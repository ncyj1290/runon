package com.cyj.runon.mapper;

import com.cyj.runon.vo.MemberVO;

public interface MemberMapper {
    
    // 로그인용 회원 정보 조회
    MemberVO getMemberInfoById(String member_id);
    
    // 회원 가입
    int insertMember(MemberVO member);
    
    // 회원 정보 수정
    int updateMember(MemberVO member);
    
    // 회원 탈퇴
    int deleteMember(String member_id);
    
}