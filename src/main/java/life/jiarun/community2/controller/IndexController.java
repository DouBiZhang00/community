package life.jiarun.community2.controller;

import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String index(HttpServletRequest request){
        //从客户端的请求中得到全部cookies
        Cookie[] cookies = request.getCookies();
        //cookie非空情况下遍历全部cookie，若cookie名字为token，使用缓冲层对象的方法根据token查找到token对应的对象，并将该user写入session
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //写入session
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return "index";
    }
}