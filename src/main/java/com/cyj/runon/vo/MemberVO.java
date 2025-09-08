package com.cyj.runon.vo;

import lombok.Data;

@Data
public class MemberVO {
    private String member_id;
    private String member_pw;
    private String member_name;
    private String member_email;
    private String member_phone;
    private String member_type;
    private String member_status;
    private String profile_picture_url;
    private int token;
    private String created_at;
    private String updated_at;
}