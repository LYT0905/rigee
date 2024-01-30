package com.config;

import com.alibaba.fastjson.JSON;
import com.common.R;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mark
 * @date 2024/1/30
 */

/**
 * 拦截器实现
 */
//@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object employee = request.getSession().getAttribute("employee");
        if (employee != null){
            return true;
        }else {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
    }
}
