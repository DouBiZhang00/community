package life.jiarun.community2.advice;

import com.alibaba.fastjson.JSON;
import life.jiarun.community2.dto.ResultDTO;
import life.jiarun.community2.exception.CustomizeErrorCode;
import life.jiarun.community2.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//注解定义全局异常处理类
//优点：将 Controller 层的异常和数据校验的异常进行统一处理，减少模板代码，减少编码量，提升扩展性和可维护性。
//缺点：只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了。
@ControllerAdvice
//控制器增强处理类，配合异常处理注解，将控制器异常处理方法导航拓展到全部控制类。
public class CustomizeExceptionHandler {
    //注解声明全部异常处理方法
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
//处理请求content-type为application/json的异常
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO = null;
            if (e instanceof CustomizeException) {
                //自定义异常处理
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                //系统异常
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            //构造回应，设置内容类型，状态码，字符集，将对象转化为JSON写入输出流
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {

            }
            return null;
        } else {
            //处理请求为其他内容类型的异常
            //如果遇到自定义异常，将打印信息增加进入model，返回页面
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }


    }
}






























