package life.jiarun.community2.mapper;

import life.jiarun.community2.model.Comment;
import life.jiarun.community2.model.Question;
import org.springframework.stereotype.Service;

@Service
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}
