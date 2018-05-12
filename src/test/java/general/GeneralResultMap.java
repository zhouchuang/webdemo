package general;


import jdk.nashorn.internal.ir.annotations.Ignore;
import user.zc.aop.StorageChild;
import user.zc.models.base.User;
import user.zc.utils.BaseEntity;
import user.zc.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * 生成级联对象映射
 *
 * @author:Administrator
 * @create 2018-02-01 11:20
 */
public class GeneralResultMap {
    public static String mappre = "reply";
    public static void main(String[] args) {
        Class clazz = User.class;
        StringBuffer sb = new StringBuffer("<!--级联查询-->\n<sql id=\""+mappre+"detailList\">");
        column(clazz,"","",sb);
        sb.append("\n</sql>\n");
        System.out.println(sb.toString());




        parse(clazz,"");


        StringBuffer leftjoin = new StringBuffer("<!--对象的多级映射 mapList mapPage mapOne-->\n<select id=\""+mappre+"mapPage\" resultMap=\""+mappre+clazz.getSimpleName()+"Map\" >\n\tselect\n\t<include refid=\""+mappre+"detailList\"/>\n\tfrom  "+clazz.getSimpleName().toLowerCase()+" "+clazz.getSimpleName().substring(0,1).toLowerCase());
        leftJoin(clazz,clazz.getSimpleName().substring(0,1).toLowerCase(),leftjoin);
        leftjoin.append("\n\t<include refid=\"whereClause\"/>\n" +
                "\t<if test=\"orderByClause!=null\">${orderByClause}</if>\n" +
                "</select>");
        System.out.println(leftjoin.toString());


        System.out.println("public PageParam mapPage(PageParam pageParam) throws Exception {\n" +
                "\t\tpageParam.setList(this.getDao().mapPage(pageParam));\n" +
                "\t\treturn  pageParam;\n" +
                "\t}");

        System.out.println("List<"+clazz.getSimpleName()+"> mapPage(PageParam pageParam);");
    }

    private static void parse(Class clazz,String pre){

        StringBuffer stringBuffer  = new StringBuffer();
        stringBuffer.append("<resultMap id=\""+mappre+clazz.getSimpleName()+"Map\" type=\""+clazz.getSimpleName()+"\">\n");
        stringBuffer.append("\t<id property=\"id\" column=\""+pre+"id\" />\n");
        stringBuffer.append("\t<result property=\"version\" column=\""+pre+"version\" />\n");
        for(Field field : clazz.getDeclaredFields()){
            if(check(field) ){
                if(BaseEntity.class.isAssignableFrom(field.getType())){
                    stringBuffer.append("\t<association property=\""+field.getName()+"\"  javaType=\""+field.getType().getSimpleName()+"\" resultMap=\""+mappre+field.getType().getSimpleName()+"Map\"/>\n" );
                    parse(field.getType(),pre+field.getName()+".");
                }else if(  List.class.isAssignableFrom(field.getType()) ){
                    //得到泛型里的class类型对象
                    Class<?> genericClazz = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    stringBuffer.append("\t<collection property=\""+field.getName()+"\" resultMap=\""+mappre+genericClazz.getSimpleName()+"Map\" />\n" );
                    parse(genericClazz,pre+field.getName()+".");
                }else if(!Map.class.isAssignableFrom(field.getType())){ //不为map类型
                    stringBuffer.append("\t<result property=\""+field.getName()+"\" column=\""+pre+field.getName()+"\" />\n" );
                }
            }
        }
        stringBuffer.append("</resultMap>");
        System.out.println("<!--"+(mappre+clazz.getSimpleName())+"映射-->");
        System.out.println(stringBuffer.toString()+"\n\r");
    }

    private static boolean check(Field field){
        String level = Modifier.toString(field.getModifiers());
        if(level.startsWith("public"))return false;
        Annotation ignore =  field.getAnnotation(Ignore.class);
        if(ignore==null){
            if(BaseEntity.class.isAssignableFrom(field.getType()) ||  List.class.isAssignableFrom(field.getType()) ){
                Annotation mapIdentification =  field.getAnnotation(StorageChild.class);
                return mapIdentification!=null;
            }else if(!Map.class.isAssignableFrom(field.getType())){
                return !field.getName().equals("serialVersionUID") ;
            }
        }
        return false;
    }

    private static void column(Class clazz,String fieldName,String pre,StringBuffer stringBuffer){
        stringBuffer.append("\n"+(StringUtils.isEmpty(pre)?clazz.getSimpleName().toLowerCase().substring(0,1):fieldName)+".id as '"+ pre+"id"+"',");
        stringBuffer.append("\n"+(StringUtils.isEmpty(pre)?clazz.getSimpleName().toLowerCase().substring(0,1):fieldName)+".version as '"+ pre+"version"+"'");
        for(Field field : clazz.getDeclaredFields()){
            if( check(field) ){
                if(BaseEntity.class.isAssignableFrom(field.getType())   ){
                    stringBuffer.append(",");
                    column(field.getType(),field.getName(),pre+field.getName()+".",stringBuffer);
                }else if( List.class.isAssignableFrom(field.getType())  ){
                    Class<?> genericClazz = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    stringBuffer.append(",");
                    column(genericClazz,field.getName(),pre+field.getName()+".",stringBuffer);
                }else{
                    stringBuffer.append(","+(StringUtils.isEmpty(pre)?clazz.getSimpleName().toLowerCase().substring(0,1):fieldName)+"."+field.getName()+" as '"+ pre+field.getName()+"'");
                }
            }
        }
    }

    private static void leftJoin(Class clazz,String fieldName,StringBuffer stringBuffer){
        for( Field field : clazz.getDeclaredFields()){
            if(check(field)){
                if( BaseEntity.class.isAssignableFrom(field.getType()) || List.class.isAssignableFrom(field.getType())  ){
                    StorageChild mapIdentification =  field.getAnnotation(StorageChild.class);
                    Class  childClazz  ;
                    if(List.class.isAssignableFrom(field.getType()) ){
                        childClazz = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    }else{
                        childClazz = field.getType();
                    }
                    String tableName = childClazz.getSimpleName().toLowerCase();
                    if(StringUtils.isNotEmpty(mapIdentification.refColumn())){
                        if(StringUtils.isNotEmpty(mapIdentification.revColumn())){
                            stringBuffer.append("\n\tleft join "+ tableName  + " " + field.getName() + " on "+fieldName+ "."+ mapIdentification.refColumn() +" = "+ field.getName()+"."+mapIdentification.revColumn());
                        }else{
                            stringBuffer.append("\n\tleft join "+ tableName + " " + field.getName() + " on "+fieldName+ "."+ mapIdentification.refColumn() +" = "+ field.getName()+".id");
                        }
                    }else{
                        stringBuffer.append("\n\tleft join "+ tableName  + " " + field.getName() + " on "+fieldName+ ".id = "+ field.getName()+"."+mapIdentification.revColumn());
                    }
                    leftJoin(childClazz,field.getName(),stringBuffer);
                    /*if(List.class.isAssignableFrom(field.getType()) ){
                        Class<?> genericClazz = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                        leftJoin(genericClazz,field.getName(),stringBuffer);
                    }else{
                        leftJoin(field.getType(),field.getName(),stringBuffer);
                    }*/
                }
                /*else if( List.class.isAssignableFrom(field.getType())  ){
                    StorageChild mapIdentification =  field.getAnnotation(StorageChild.class);
                    Class<?> genericClazz = (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    stringBuffer.append("\n\tleft join "+ genericClazz.getSimpleName().toLowerCase()  + " " + field.getName() + " on "+fieldName+ ".id = "+ field.getName()+"."+mapIdentification.revColumn());
                    leftJoin(genericClazz,field.getName(),stringBuffer);
                }*/
            }
        }
    }
}
