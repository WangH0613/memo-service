package cn.com.eccom.schememodelam.config;

import cn.com.koriesh.memo.common.ErrorCodeEnum;
import cn.com.koriesh.memo.config.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice extends BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HashMap<String, Object> error1(Exception e) {
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getDescription()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public HashMap<String, Object> NullPointerExceptionAdvice(Exception e) {
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getDescription()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public HashMap<String, Object> RuntimeExceptionAvice(Exception e) {
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0010.getDescription()
        );
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public HashMap<String, Object> IOExceptionAdvice(Exception e) {
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0011.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0011.getDescription()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public HashMap<String, Object> AuthenticationExceptionAdvice(Exception e) {
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0012.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0012.getDescription()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public HashMap<String, Object> UnauthorizedExceptionAdvice(Exception e) {
        log.error("用户权限不足 {}", e.getMessage());
        log.error(this.getStackTrace(e));
        return this.getResMap(
                ErrorCodeEnum.SYSTEM_ERROR_B0013.getCode(),
                ErrorCodeEnum.SYSTEM_ERROR_B0013.getDescription()
        );
    }

    /**
     * @Author: wangh
     * @Description: 通过Exception对象  简单过滤出与本系统相关的异常栈
     * @Date: 2020/11/16
     * @Param: [e]
     * @Return: java.lang.String
     */
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder("\n");
        StackTraceElement[] arrs = e.getStackTrace();
        for (StackTraceElement stack : arrs) {
            if (stack != null) {
                String stackStr = stack.toString();
                sb.append(stackStr + "\n");
            }
        }
        return sb.toString();
    }
}
