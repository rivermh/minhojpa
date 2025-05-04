package com.minhojpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minhojpa.entity.Member;

@Repository // Repository로 등록
public interface MemberRepository extends JpaRepository<Member, Long> {
	// 기본적인 CRUD 메서드들은 JpaRepository가 제공해줌
	// 만약에 추가적인 메서드가 필요하면 이곳에 추가할 수 있음
	
	/*JpaRepository가 제공하는 기본 메소드
	save() 엔티티를 저장하거나 업데이트
	findById() ID로 엔티티를 조회
	findAll() 모든 엔티티를 조회
	deleteById() ID로 엔티티를 삭제
	count() 엔티티 개수 조회*/
}
