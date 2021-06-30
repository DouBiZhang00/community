package life.jiarun.community2.controller;

import life.jiarun.community2.cache.HotTagCache;
import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag) {

        List<String> tags = hotTagCache.getHots();
        //将包含User类对象的新Question对象数组放入model显示
//        List<QuestionDTO> questionDTOList = questionService.list(page,size);
//        model.addAttribute("questions", questionDTOList);
        //使用分页对象包含QuestionDTO
        PaginationDTO<QuestionDTO> pagination = questionService.list(search, tag, page, size);
        model.addAttribute("search", search);
        //放入model提供交互显示
        model.addAttribute("pagination", pagination);
        model.addAttribute("tags", tags);
        model.addAttribute("tag", tag);
        return "index";
    }
}