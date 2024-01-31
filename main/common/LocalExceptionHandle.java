package com.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author Mark
 * @date 2024/1/31
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
@ResponseBody
public class LocalExceptionHandle {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        String[] s = message.split(" ");
        return R.error(s[2] + "账号已存在，请重新输入");
    }
}
