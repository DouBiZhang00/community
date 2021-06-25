package life.jiarun.community2.dto;

import life.jiarun.community2.model.User;
import lombok.Data;

//首页问题列表展示需要头像显示，将持久层Question对象拓展为包含User类的对象
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
