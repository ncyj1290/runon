package com.cyj.runon.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import com.cyj.runon.mapper.MemberMapper;
import com.cyj.runon.vo.MemberVO;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO member = memberMapper.getMemberInfoById(username);

        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 탈퇴된 회원인지 먼저 확인 (아이디/비밀번호 체크 전에)
        if ("INACTIVE".equals(member.getMember_status())) {
            throw new DisabledException("이미 탈퇴처리된 회원입니다.");
        }

        return new CustomUserDetails(member);
    }
}