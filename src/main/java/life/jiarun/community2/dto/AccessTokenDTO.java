package life.jiarun.community2.dto;

import lombok.Data;

//Lombok@Date标签自动根据属性生成Get/Set方法
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
