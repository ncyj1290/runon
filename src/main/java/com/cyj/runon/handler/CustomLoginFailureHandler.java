package com.cyj.runon.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component("customLoginFailureHandler")
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage;
        
        // 탈퇴된 회원인 경우 특별한 메시지 표시
        if (exception instanceof DisabledException) {
            errorMessage = "이미 탈퇴처리된 회원입니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // InternalAuthenticationServiceException 내부에 DisabledException이 있는지 확인
            Throwable cause = exception.getCause();
            if (cause instanceof DisabledException) {
                errorMessage = "이미 탈퇴처리된 회원입니다.";
            } else {
                errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
            }
        } else {
            errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
        }
        
        // 1. 로그인 실패 시, 에러 메시지를 세션(Session)에 저장합니다.
        //    (URL 파라미터가 아니라 세션에 저장하는 것이 핵심!)
        request.getSession().setAttribute("loginErrorMessage", errorMessage);
        
        // 2. 에러 파라미터가 없는 '깨끗한' 로그인 페이지 URL로 리다이렉트합니다.
        response.sendRedirect(request.getContextPath() + "/login");
    }
}