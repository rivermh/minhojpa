package com.minhojpa.service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public LoginService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/* 이메일과 비밀번호 일치하는 회원 확인 (bcrypt 비교) */
	public Member login(String email, String rawPassword) {
		Optional<Member> optionalMember = memberRepository.findByEmail(email);

		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();

			// 암호화된 비밀번호와 입력값 비교
			if (passwordEncoder.matches(rawPassword, member.getPassword())) {
				return member;
			}
		}
		return null;
	}
}
