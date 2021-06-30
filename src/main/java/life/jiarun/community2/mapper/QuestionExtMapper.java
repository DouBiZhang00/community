package life.jiarun.community2.mapper;

import life.jiarun.community2.dto.QuestionQueryDTO;
import life.jiarun.community2.model.Question;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionExtMapper {
    int incView(Question question);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}
