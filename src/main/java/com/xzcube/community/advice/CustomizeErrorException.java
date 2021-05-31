package com.xzcube.community.advice;

import com.alibaba.fastjson.JSON;
import com.xzcube.community.dto.ResultDTO;

import com.xzcube.community.exception.CustomizeErrorCode;
import com.xzcube.community.exception.CustomizeException;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ControllerAdvice 注解，可以用于定义@ExceptionHandler、@InitBinder、@ModelAttribute，
 * 并应用到所有@RequestMapping中。
 *
 * 给方法加上@ExceptionHandler注解，这个方法就会处理类中其他方法（被@RequestMapping注解）抛出的异常。
 *
 * @author xzcube
 * @date 2021/5/31 9:44
 */
@ControllerAdvice
public class CustomizeErrorException {
    @ExceptionHandler(Exception.class)
    Object exceptionHandler(HttpServletRequest request, Throwable e, Model model,
                            HttpServletResponse response){

        HttpStatus status = getStatus(request);
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            ResultDTO resultDTO;
            // 返回JSON
            if(e instanceof CustomizeException){
                // 如果是我们自定义的异常
                resultDTO =  ResultDTO.errorOf((CustomizeException) e);
            }else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;

        }else {
            // 错误页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("errorMessage", e.getMessage());
            }else {
                model.addAttribute("errorMessage", "服务器冒烟了...");
            }
            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
