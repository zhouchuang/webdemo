package user.zc.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import user.zc.models.base.User;
import user.zc.service.base.PermissionService;
import user.zc.service.base.RoleService;

import javax.servlet.http.HttpServletRequest;

/**
 * 主要的控制层
 *
 * @author zhouchuang
 * @create 2018-05-12 10:07
 */
@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    PermissionService permissionService;
    @Autowired
    RoleService roleService;

    @RequestMapping(value="/indexMain")
    public String indexMain(Model model , HttpServletRequest request) {
        return "/index";
    }

    @RequestMapping(value="/loginMain")
    public String loginMain(Model model , HttpServletRequest request) {
        return "/login";
    }

    /**
     *登录
     */
    @RequestMapping("/toLogin")
    public String toLogin(Model model ,User user){
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccountNo(),user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        token.setRememberMe(true);
        try {
            subject.login(token);
            subject.getSession().setAttribute("user",user);
            subject.getSession().setAttribute("menus",permissionService.findUserPermissionList(roleService.findRoleIdByAccountNo(user.getAccountNo())));
            return "redirect:/indexMain";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            model.addAttribute("result","用户名或密码错误");
            return "redirect:/loginMain";
        }
    }
}
