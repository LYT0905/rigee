package com.filter;


import com.alibaba.fastjson.JSON;
import com.common.BaseContext;
import com.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mark
 * @date 2024/1/30
 */

/**
 * 登录过滤器
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 设置不过滤的路径
        String[] urls = new String[]{"/backend/**", "/front/**", "/employee/login", "/employee/logout"};
        String requestURI = request.getRequestURI();
        Long employee = (Long) request.getSession().getAttribute("employee");
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request, response);
            return;
        }
        if (employee != null){
            BaseContext.setId(employee);
            filterChain.doFilter(request, response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查路径是否需要进行拦截
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean check(String[] urls, String requestURI) {
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
