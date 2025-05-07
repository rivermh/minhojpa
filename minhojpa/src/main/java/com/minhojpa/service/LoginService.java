package com.minhojpa.service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 이메일과 이름이 일치하는 회원이 있는지 확인
     */
    public Member login(String email, String password) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 비밀번호 확인 (평문 비교)
            if (member.getPassword().equals(password)) {
                return member;
            }
        }
        return null;
    }
}
