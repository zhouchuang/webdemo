package general;


import user.zc.models.base.Permission;

import java.lang.reflect.Field;

/**
 * @author:Administrator
 * @create 2018-01-19 14:50
 */
public class MyBatisTools {
    public static void add(Class clazz){
        String str = " insert into "+clazz.getName() + "\n\r(";
        String key = "";
        String value = "";
        for(Field field : clazz.getDeclaredFields()){
            key += field.getName()+",";
            value += "#{"+field.getName()+"},";
        }
        for(Field field : clazz.getSuperclass().getDeclaredFields()){
            key += field.getName()+",";
            value += "#{"+field.getName()+"},";
        }
        key = key.substring(0,key.length()-1);
        value = value.substring(0,value.length()-1);
        str += key+")\r\nvalues("+value+")";

        System.out.println(str);

    }

    public static void main(String[] args){
        MyBatisTools.add(Permission.class);
    }
}
