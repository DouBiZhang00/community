package life.jiarun.community2.controller;

import life.jiarun.community2.dto.CommentDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.enums.CommentTypeEnum;
import life.jiarun.community2.service.CommentService;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name="id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        //根据questionDTO查询相同标签的问题，返回为list
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //累加阅读数
        questionService.incView(id);
        //根据输入的类型和id决定评论的是问题还是评论
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}

