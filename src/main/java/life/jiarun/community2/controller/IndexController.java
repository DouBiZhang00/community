package life.jiarun.community2.controller;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size){

        //将包含User类对象的新Question对象数组放入model显示
//        List<QuestionDTO> questionDTOList = questionService.list(page,size);
//        model.addAttribute("questions", questionDTOList);
        //使用分页对象包含QuestionDTO
        PaginationDTO<QuestionDTO> pagination =  questionService.list(page,size);
        //放入model提供交互显示
        model.addAttribute("pagination",pagination);
        return "index";
    }
}