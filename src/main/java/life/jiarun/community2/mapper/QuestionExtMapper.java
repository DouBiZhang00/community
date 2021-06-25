package life.jiarun.community2.mapper;

import life.jiarun.community2.model.Question;
import org.springframework.stereotype.Service;

@Service
public interface QuestionExtMapper {
    int incView(Question question);
    int incCommentCount(Question record);
}
