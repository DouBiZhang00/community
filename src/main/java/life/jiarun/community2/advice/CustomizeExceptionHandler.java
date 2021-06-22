package life.jiarun.community2.advice;

import life.jiarun.community2.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//注解定义全局异常处理类
//优点：将 Controller 层的异常和数据校验的异常进行统一处理，减少模板代码，减少编码量，提升扩展性和可维护性。
//缺点：只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了。
@ControllerAdvice
//控制器增强处理类，配合异常处理注解，将控制器异常处理方法导航拓展到全部控制类。
public class CustomizeExceptionHandler {
//注解声明全部异常处理方法
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model){
        //如果遇到自定义异常，将打印信息增加进入model
        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }
        else {
            model.addAttribute("message","服务冒烟了，要不稍后你再试试！！！");
        }
        return new ModelAndView("error");
    }
}
