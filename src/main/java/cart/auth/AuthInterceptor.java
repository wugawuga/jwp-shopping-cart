package cart.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return true;
    }

}