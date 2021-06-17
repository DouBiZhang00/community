package life.jiarun.community2.controller;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.User;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "2")Integer size){
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
        //将包含User类对象的新Question对象数组放入model显示
//        List<QuestionDTO> questionDTOList = questionService.list(page,size);
//        model.addAttribute("questions", questionDTOList);
        PaginationDTO pagination =  questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}