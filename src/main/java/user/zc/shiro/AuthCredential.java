package user.zc.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import user.zc.utils.MD5Tools;

/**
 * 算法
 *
 * @author:Administrator
 * @create 2018-01-19 10:52
 */
public class AuthCredential extends SimpleCredentialsMatcher {

    //加密算法需要和注册时一致，即调用同一个MD5工具类，放入相同的参数
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info){
        UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
        String username = loginToken.getUsername();
        char[] password = loginToken.getPassword();
        String p = String.valueOf(password);
        String ps = MD5Tools.getMD5Hash(username, p);

        loginToken.setPassword(ps.toCharArray());

        return super.doCredentialsMatch(loginToken, info);
    }

    public static void main(String[] args) {
        System.out.println(MD5Tools.getMD5Hash("zp", "123456"));
    }
}