package com.minhojpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
import com.minhojpa.repository.MemberRepository;

@Service
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	// 모든 회원 조회
	public List<Member> findAllmembers(){
		return memberRepository.findAll();
	}
	
	// ID로 회원 조회
	public Member findMemberById(Long id) {
		Optional<Member> member = memberRepository.findById(id);
		return member.orElse(null);
	}
	
	// 회원 삭제
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}
	
	// 회원 저장 (비밀번호 암호화 없이)
	public Member saveMember(Member member) {
		return memberRepository.save(member); // 비밀번호 암호화 없이 저장
	}
}
