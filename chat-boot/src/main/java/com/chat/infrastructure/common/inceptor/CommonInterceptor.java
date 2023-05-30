package com.chat.infrastructure.common.inceptor;

import com.chat.infrastructure.common.UserContext;
import com.chat.infrastructure.po.UmsMember;
import com.chat.infrastructure.util.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.chat.infrastructure.util.JwtTokenUtils.TOKEN_HEADER;
import static com.chat.infrastructure.util.JwtTokenUtils.TOKEN_PREFIX;

@Component
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(TOKEN_HEADER);
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            response.setStatus(401);
            return false;
        }
        String token = header.split(" ")[1].trim();
        UmsMember umsMember = JwtTokenUtils.getJwtTokenLoginUser(token);
        if (Objects.isNull(umsMember)){
            response.setStatus(401);
            return false;
        }
        UserContext.setUser(umsMember);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clearUser();
    }
}
