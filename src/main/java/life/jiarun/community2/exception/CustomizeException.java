package life.jiarun.community2.exception;

public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    public CustomizeException(String message){
        this.message = message;
    }

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message =errorCode.getMessage();
    }

    @Override
    public String getMessage(){
        return message;
    }

    public Integer getCode() { return code; }
}
