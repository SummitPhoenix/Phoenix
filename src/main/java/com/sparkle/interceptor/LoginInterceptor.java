package com.sparkle.interceptor;

import com.sparkle.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description 登录验证拦截
 * @Author: XuanXiangHui
 * @Date: 2019/12/6 下午2:13
 */
@Controller
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String basePath = request.getContextPath();
        String path = request.getRequestURI();

        if(!doLoginInterceptor(path, basePath) ){//是否进行登陆拦截
            return true;
        }

        String token = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 遍历数组
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
        if(0L == JWTUtil.verify(token)){
            logger.info("尚未登录，跳转到登录界面");
            response.sendRedirect(request.getContextPath()+"login");
            return false;
        }

        logger.info("用户已登录,userName:"+JWTUtil.parseJWT(token).get("username"));
        return true;
    }

    /**
     * 是否进行登陆过滤
     * @param path
     * @param basePath
     * @return
     */
    private boolean doLoginInterceptor(String path,String basePath){
        path = path.substring(basePath.length());
        Set<String> notLoginPaths = new HashSet<>();
        //设置不进行登录拦截的路径：登录注册和验证码
        notLoginPaths.add("/index");
        notLoginPaths.add("/login");
        notLoginPaths.add("/reg");

        if(notLoginPaths.contains(path)){
            return false;
        }
        return true;
    }
}
