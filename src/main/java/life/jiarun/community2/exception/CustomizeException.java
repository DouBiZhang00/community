package life.jiarun.community2.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(String message){
        this.message = message;
    }

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message =errorCode.getMessage();
    }

    @Override
    public String getMessage(){
        return message;
    }
}
