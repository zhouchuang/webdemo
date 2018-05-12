package general;


import user.zc.models.base.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

/**
 * json生成工具
 *
 * @author:Administrator
 * @create 2018-01-24 12:38
 */
public class JsonGeneralTools {
    public static void main(String[] args) {
        String str = "{";
        for(Field field : User.class.getDeclaredFields()){
            if(ignoreField(field)){
                str += "\""+field.getName()+"\":"+getValue(field)+",";
            }
        }
        str= str.substring(0,str.length()-1);
        str += "}";
        System.out.println(str);
    }
    public static boolean ignoreField(Field field){
        if(field.getName().equals("serialVersionUID"))return false;
        else{
            if(field.getGenericType()==String.class ||
                    field.getGenericType()==Integer.class||
                    field.getGenericType()==Boolean.class||
                    field.getGenericType()==Date.class||
                    field.getGenericType()==BigDecimal.class||
                    field.getGenericType()==Long.class){
                return true;
            }
            return false;
        }
    }
    public static String getValue(Field field){
        if(field.getGenericType()==String.class){
            return "\"abc\"";
        }else if(field.getGenericType()==Boolean.class){
            return "false";
        }else if(field.getGenericType()==Integer.class||field.getGenericType()==Long.class||field.getGenericType()== BigDecimal.class){
            return "1";
        }else if(field.getGenericType()==Date.class){
            return "\"2018/01/01\"";
        }else{
            return "";
        }
    }
}
