package com.example.project_demo1.service;

import com.example.project_demo1.model.Member;
import com.example.project_demo1.network.Result;

public interface MemberService {

    Result login(String email, String password);

    Result registration(Member newMember);

    boolean isLogin();

    void logout();
}
