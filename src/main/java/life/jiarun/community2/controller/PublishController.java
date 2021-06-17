package life.jiarun.community2.controller;

import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model
            ){
        //与前端交互的模型数据Model类对象先加入填入表格的属性，方便检验填入错误
        //前端标签内部可以通过 th:value="${title} 绑定表单数据
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        //检验为空错误
        if(title == null || title==""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if(description == null || description==""){
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if(tag == null || tag==""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        User user = null;
        //点入发布界面时，系统会自动根据本地存入的cookie进行自动登录。如果本地没有登录过存入cookie，提示用户未登录错误
        Cookie [] cookies = request.getCookies();
        for(Cookie cookie :cookies){
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                user = userMapper.findByToken(token);
                if(user!=null){
                    request.getSession().setAttribute("user", user);
                }
                break;
            }
        }

        if(user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        //创建问题对象，存入数据，同时通过问题持久层对象将数据存入数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        //持久层对象插入数据库
        questionMapper.create(question);
        return "redirect:/";
    }


}






















































