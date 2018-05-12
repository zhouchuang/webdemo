package user.zc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 表设计控制层
 *
 * @author zhouchuang
 * @create 2018-05-10 23:35
 */
@Controller
@RequestMapping("/TableController")
public class TableController {

    @RequestMapping(value="/index")
    public String index(Model model , HttpServletRequest request) {
        return "/table/index";
    }
}
