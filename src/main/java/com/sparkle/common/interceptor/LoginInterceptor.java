package com.sparkle.common.interceptor;

import com.sparkle.util.JWTUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            String token = "";
            for(Cookie cookie:cookies){
                if("token".equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
            if("".equals(token)){
                return setFailResponse(response);
            }
            if(JWTUtil.verify(token)){
                return true;
            }
        }
        return setFailResponse(response);
    }

    private boolean setFailResponse(HttpServletResponse response) throws IOException {
        response.setHeader("content-type", "text/json;charset=UTF-8");
        response.getWriter().println("您尚未登录");
        return false;
    }
}
