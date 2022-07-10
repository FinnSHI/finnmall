package com.finn.gulimall.cart.interceptor;

import com.finn.common.constant.AuthServerConstant;
import com.finn.common.constant.CartConstant;
import com.finn.common.vo.MemberResponseVO;
import com.finn.gulimall.cart.to.UserInfoTO;
import org.apache.catalina.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;

/*
 * @description: 业务执行之前的拦截器，在执行目标前，判断用户用户状态，并封装传递给controller目标请求
 * @author: Finn
 * @create: 2022/07/10 14:06
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTO> threadLocal = new ThreadLocal<>();

    /*
    * 在目标方法之前拦截
    */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfoTO userInfoTO = new UserInfoTO();
        HttpSession session = request.getSession();
        // session保存的是用户详细信息MemberResponseVO
        MemberResponseVO member = (MemberResponseVO) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (!Objects.isNull(member)) {
            // session里有用户信息，用户已经登录
            userInfoTO.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (!Objects.isNull(cookies) && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                // 如果有临时身份（key为user-key的cookie）x
                if (CartConstant.TEMP_USER_COOKIE_NAME.equals(name)) {
                    // 保存user-key的cookie的value
                    userInfoTO.setUserKey(cookie.getValue());
                    // 标记为已是临时用户
                    userInfoTO.setTempUser(true);
                }
            }
        }

        //如果没有临时用户一定分配一个临时用户
        if (StringUtils.isEmpty(userInfoTO.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTO.setUserKey(uuid);
        }

        // 将信息保存在ThreadLocal里
        threadLocal.set(userInfoTO);

        return true;
    }

    /*
    * @Description: 业务执行之后的拦截器
    * @Param: [request, response, handler, modelAndView]
    * @return: void
    * @Author: Finn
    * @Date: 2022/07/10 14:55
    */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        // 获取当前用户的值
        UserInfoTO userInfoTO = threadLocal.get();

        // 如果没有临时用户身份，就保存一个临时用户身份
        if (!userInfoTO.getTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTO.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
