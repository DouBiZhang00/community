package life.jiarun.community2.service;

import life.jiarun.community2.dto.PaginationDTO;
import life.jiarun.community2.dto.QuestionDTO;
import life.jiarun.community2.dto.QuestionQueryDTO;
import life.jiarun.community2.enums.SortEnum;
import life.jiarun.community2.exception.CustomizeErrorCode;
import life.jiarun.community2.exception.CustomizeException;
import life.jiarun.community2.mapper.QuestionExtMapper;
import life.jiarun.community2.mapper.QuestionMapper;
import life.jiarun.community2.mapper.UserMapper;
import life.jiarun.community2.model.Question;
import life.jiarun.community2.model.QuestionExample;
import life.jiarun.community2.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//将Question表对应持久层对象拓展包含User对象的服务，同时为显示页码，服务结合分页模块，涵盖Question新对象
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    //首页展示问题处理方法，输入页码和每页包含元素的数量，返回分页对象
    public PaginationDTO list(String search, String tag,String sort,Integer page, Integer size) {
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);

        if (StringUtils.isNotBlank(tag)) {
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase().equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }

        Integer totalPage;
        //创建paginationDTO对象同时将所需参数传入，将页面显示问题页码模块功能所需参数矫正
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        //使用mybatis generator 取代原来mybatis注解的方式
        Integer totalCount =  questionExtMapper.countBySearch(questionQueryDTO);
        //得到总页数，并对传入页数参数进行矫正
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
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
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
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //根据id筛选返回分页对象
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        Integer totalPage;
        //创建paginationDTO对象同时将所需参数传入，将页面显示问题页码模块功能所需参数矫正
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO();
        //表对应的Example类负责拼接sql语句
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
        paginationDTO.setData(questionDTOList);
        return paginationDTO;

    }

    //根据问题id返回questionDTO对象，便于编辑
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

    //创建或更新问题
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setSticky(0);
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

    //增加阅读数的方法
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
    //输入问题，返回使用过相同问题tag的问题集
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        //通过正则组合
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);

        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}









































