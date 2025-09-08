package com.cyj.runon.handler;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler; 
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.cyj.runon.service.MemberService;
import com.cyj.runon.vo.MemberVO;

@Component("customLoginSuccessHandler")
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { 

    @Autowired
    private MemberService service; 
	
    // Spring Security가 '원래 가려던 주소'를 저장하는 객체
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        // --- 1. 부가 작업 수행 ---
        handleRememberIdCookie(request, response, authentication);
        saveUserToSession(request, authentication);
        
        // Spring Security가 저장해 둔 '원래 가려던 주소'가 있는지 확인
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        
        if (savedRequest != null) {
            // '원래 가려던 주소'가 있다면, 그곳으로 바로 보내줍니다.
            String targetUrl = savedRequest.getRedirectUrl();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            // '원래 가려던 주소'가 없다면 (직접 로그인 페이지로 온 경우),
            // 역할별 기본 페이지로 보내줍니다.
            String targetUrl = determineTargetUrl(authentication);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
    
    /**
     * 역할(Role)에 따라 '기본' 이동 URL을 결정하는 메소드
     */
    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            
            if (authorityName.equals("ROLE_ADMIN")) { 
                return "/adminMain"; 
            } else if (authorityName.equals("ROLE_USER")) { 
                return "/main";
            }
        }

        return "/"; 
    }
    
    
    // 아이디 유지 
    private void handleRememberIdCookie(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberId = request.getParameter("rememberId");
        String member_id = authentication.getName();
        
        Cookie idCookie = new Cookie("rememberId", member_id);
        idCookie.setPath(request.getContextPath() + "/");
        
        if (rememberId != null && rememberId.equals("true")) {
            idCookie.setMaxAge(60 * 60 * 24 * 30);
        } else {
            idCookie.setMaxAge(0);
        }
        response.addCookie(idCookie);
    }
    
    // 사용자가 로그인한 순간의 정보를 세션에 담음
    private void saveUserToSession(HttpServletRequest request, Authentication authentication) {
        String member_id = authentication.getName();
        MemberVO loginUser = service.getMemberInfoById(member_id);
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
    }
}