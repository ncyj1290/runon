package com.cyj.runon.service;

import com.cyj.runon.vo.MemberVO;

public interface MemberService {
    
    // 로그인용 회원 정보 조회
    MemberVO getMemberInfoById(String member_id);
    
    // 회원 가입
    boolean registerMember(MemberVO member);
    
    // 회원 정보 수정
    boolean updateMemberInfo(MemberVO member);
    
    // 회원 탈퇴
    boolean withdrawMember(String member_id);
    
}