package com.cyj.runon.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        // 부모 클래스의 생성자에는 '기본' 로그인 URL을 전달합니다.
        super(loginFormUrl);
    }

    /**
     * 이 요청에 대해 어떤 로그인 폼 URL을 사용할지 결정하는 메소드를 재정의(Override)합니다.
     */
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
        
        // 1. 사용자가 원래 요청했던 주소(URL)를 가져옵니다.
        String requestedUrl = request.getRequestURI();
        
        // 2. 만약 원래 가려던 주소가 "admin"을 포함한다면,
        if (requestedUrl != null && requestedUrl.contains("admin")) {
            // "이번에는 관리자 로그인 페이지 URL을 사용해!" 라고 반환합니다.
            return "/adminLogin"; 
        }

        // 3. 그 외의 모든 경우는, 부모 클래스의 기본 동작을 따릅니다.
        //    (부모 클래스는 생성자에서 받은 기본 로그인 URL을 반환합니다.)
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
    
    // 인증되지 않은 사용자 요청 들어올 때 호출
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // "X-Requested-With" 헤더를 확인하여 AJAX 요청인지 판별합니다.
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
            //  AJAX 요청이면: 401 Unauthorized 에러를 응답합니다.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            //  일반 페이지 요청이면: 기존 로직(로그인 페이지로 리다이렉트)을 그대로 수행합니다.
            super.commence(request, response, authException);
        }
    }

}