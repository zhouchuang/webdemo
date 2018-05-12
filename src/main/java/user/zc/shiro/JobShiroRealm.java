package user.zc.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import user.zc.models.base.User;
import user.zc.service.base.UserService;
import user.zc.utils.QueryParam;


/**
 * job权限验证
 *
 * @author:Administrator
 * @create 2018-01-19 10:45
 */
public class JobShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    /*
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        try {
            return this.userService.doGetAuthorizationInfo(principals);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken userToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) userToken;
        User user = null;
        try {
            user = userService.one(new QueryParam("accountNo",token.getUsername()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            if (user.getLocked()) {
                throw new LockedAccountException();
            }
            if (user.isDeleted()) {
                throw new DisabledAccountException();
            }

            String p = user.getPassword();
            return new SimpleAuthenticationInfo(user, p, this.getName());//已在配置中引入加密算法
        }else{
            throw new UnknownAccountException();
        }
    }

}
