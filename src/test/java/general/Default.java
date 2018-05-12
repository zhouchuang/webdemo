package general;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.zc.models.base.Permission;
import user.zc.models.base.Role;
import user.zc.models.base.User;
import user.zc.service.base.PermissionService;
import user.zc.service.base.RoleService;
import user.zc.service.base.UserService;
import user.zc.utils.MD5Tools;

/**
 * 生成默认账号
 *
 * @author zhouchuang
 * @create 2018-05-12 11:16
 */
@RunWith(SpringJUnit4ClassRunner.class) // 整合
@ContextConfiguration(locations={"classpath:/spring/*"}) // 加载配置
public class Default {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;


    @Test
    public void generalUser(){
        String username = "admin";
        String password = "123456";
        String hashpw = MD5Tools.getMD5Hash(username,password);
        User user = new User();
        user.setAccountNo(username);
        user.setPassword(hashpw);
        user.setNickName("创哥");
        user.setLocked(false);
        user.setPhoneNumber("18607371493");
        user.setRealName("周创");
        user.setStatus(true);
        try {
            userService.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void general()throws Exception{
        generalRole();
        generalPermission();
    }
    @Test
    public void generalRole()throws Exception{
        Role role = new Role();
        role.setName("admin");
        role.setStatus(true);
        role.setDescription("管理员");
        roleService.add(role);
    }
    @Test
    public void generalPermission()throws Exception{
        Permission permission = new Permission();
        permission.setSort("0102");
        permission.setAlias("Table:add");
        permission.setDepth(2);
        permission.setDescription("表生成器");
        permission.setName("新增");
        permission.setPlatform(1);
        permission.setUrl("/TableController/add");
        permissionService.add(permission);
    }


}
