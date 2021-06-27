package life.jiarun.community2.dto;

import life.jiarun.community2.model.User;
import org.omg.CORBA.PRIVATE_MEMBER;

import lombok.Data;
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
