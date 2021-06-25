package life.jiarun.community2.service;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.exception.CustomizeErrorCode;
import life.jiarun.community2.exception.CustomizeException;
import life.jiarun.community2.mapper.QuestionExtMapper;
import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.QuestionExample;
import life.jiarun.community2.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//将Question表对应持久层对象拓展包含User对象的服务，同时为显示页码，服务结合分页模块，涵盖Question新对象
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {

        Integer totalPage;
        //创建paginationDTO对象同时将所需参数传入，将页面显示问题页码模块功能所需参数矫正
        PaginationDTO paginationDTO = new PaginationDTO();
        //使用mybatis generator 取代原来mybatis注解的方式
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //设置limit所需的偏移量
        Integer offset = size * (page - 1);
        //通过持久层对象返回对应页码所需要的问题对象集合
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            //遍历问题，通过问题创建者Id查找创建问题的用户
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //创建问题的拓展对象
            QuestionDTO questionDTO = new QuestionDTO();
            //通过BeanUtils工具类中的方法copyProperties拷贝对象属性
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            //将拓展完成的新问题对象加入对应集合
            questionDTOList.add(questionDTO);
        }
        //将问题对象数组装入
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        Integer totalPage;
        //创建paginationDTO对象同时将所需参数传入，将页面显示问题页码模块功能所需参数矫正
        PaginationDTO paginationDTO = new PaginationDTO();
//        表对应的Example类负责拼接sql语句
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);

        //设置limit所需的偏移量
        Integer offset = size * (page - 1);
        //通过持久层对象返回对应页码所需要的问题对象集合

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            //遍历问题，通过问题创建者Id查找创建问题的用户
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            //创建问题的拓展对象
            QuestionDTO questionDTO = new QuestionDTO();
            //通过BeanUtils工具类中的方法copyProperties拷贝对象属性
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            //将拓展完成的新问题对象加入对应集合
            questionDTOList.add(questionDTO);
        }
        //将问题对象数组装入
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;

    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0 );
            questionMapper.insert(question);
        } else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}









































