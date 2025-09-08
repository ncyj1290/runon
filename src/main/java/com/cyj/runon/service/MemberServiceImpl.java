package com.cyj.runon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyj.runon.mapper.MemberMapper;
import com.cyj.runon.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberVO getMemberInfoById(String member_id) {
        return memberMapper.getMemberInfoById(member_id);
    }

    @Override
    public boolean registerMember(MemberVO member) {
        return memberMapper.insertMember(member) > 0;
    }

    @Override
    public boolean updateMemberInfo(MemberVO member) {
        return memberMapper.updateMember(member) > 0;
    }

    @Override
    public boolean withdrawMember(String member_id) {
        return memberMapper.deleteMember(member_id) > 0;
    }
}