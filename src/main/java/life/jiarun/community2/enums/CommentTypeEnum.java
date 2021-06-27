package life.jiarun.community2.enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    //枚举类内判断方法，判断是否包含评论类型
    public static boolean isExist(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if(value.type == type){
                return true;
            }
        }
        return false;
    }
}
