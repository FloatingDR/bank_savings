package com.bank.savings.handle;


import com.bank.savings.bean.ResponseBean;
import com.bank.savings.bean.ResultCode;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author taylor
 * @ClassName: ExceptionHandler
 * @Description: 自定义springboot异常
 * @date: 2019-05-19 00:33
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * token 无效
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseBean tokenInvalid(){
        return new ResponseBean(ResultCode.FORBIDDEN,"token invalid","token 无效");
    }

    @ExceptionHandler(UnknownAccountException.class)
    public ResponseBean unKnowAccount(){
        return new ResponseBean(ResultCode.FORBIDDEN,"user not found","没有该用户");
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseBean usernameOrPasswordError(){
        return new ResponseBean(ResultCode.FORBIDDEN,"username or password error","用户名或密码错误");
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseBean permissionDeniedError(){
        return new ResponseBean(ResultCode.FORBIDDEN,"ERROR Permission denied","权限不足");
    }

    /**
     * 捕捉其他所有异常
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseBean globalException() {
//        return new ResponseBean(ResultCode.FAIL,"fail","服务器内部错误");
//    }

}
