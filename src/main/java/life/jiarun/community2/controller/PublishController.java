package life.jiarun.community2.controller;

import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.User;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Integer id,
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
        //点入发布界面时，系统会自动根据本地存入的cookie进行自动登录。如果本地没有登录过存入cookie，提示用户未登录错误
        User user = (User) request.getSession().getAttribute("user");

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
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }


}






















































