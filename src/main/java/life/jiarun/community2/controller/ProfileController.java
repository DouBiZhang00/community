package life.jiarun.community2.controller;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.model.User;
import life.jiarun.community2.service.NotificationService;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public
    String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5 ")Integer size,
                          Model model) {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            return "redirect:/";
        }

        //访问的是问题/回复，根据选择增加相对应的中文提示和英文名
        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            //需要根据userId进行查询返回问题集
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }else if("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }
}































