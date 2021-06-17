package life.jiarun.community2.service;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//将Question表对应持久层对象拓展包含User对象的服务
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        if (page < 1){
            page = 1;
        }

        if(page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page - 1);
        //通过持久层对象返回问题对象集合
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question:questions){
            //遍历问题，通过问题创建者Id查找创建问题的用户
            User user = userMapper.findById(question.getCreator());
            //创建问题的拓展对象
            QuestionDTO questionDTO = new QuestionDTO();
            //通过BeanUtils工具类中的方法copyProperties拷贝对象属性
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            //将拓展完成的新问题对象加入对应集合
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}









































