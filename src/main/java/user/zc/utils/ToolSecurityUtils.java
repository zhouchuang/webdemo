package user.zc.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import user.zc.models.base.User;

import java.lang.reflect.Field;

/**
 * iJob平台的安全工具类
 *
 * @author:Administrator
 * @create 2018-01-23 13:03
 */
public class ToolSecurityUtils extends SecurityUtils {
    public static String getLoginUserId(){
        try{
            return getLoginUser().getId();
        }catch (NullPointerException e ){
            return "system";
        }

    }

    //获取登录ID
    public static User getLoginUser(){
        try{
            Object subject = SecurityUtils.getSubject().getPrincipal();
            if(subject!=null){
                return (User)subject;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Session getSession(){
        return SecurityUtils.getSubject().getSession();
    }
    public static void updateUser(User user){
        User oldUser = getLoginUser();
        for(Field field : oldUser.getClass().getDeclaredFields()){
            try {
                field.setAccessible(true);
                if(field.get(user)!=null){
                    field.set(oldUser,field.get(user));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for(Field field : oldUser.getClass().getSuperclass().getDeclaredFields()){
            try {
                field.setAccessible(true);
                if(field.get(user)!=null){
                    field.set(oldUser,field.get(user));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
