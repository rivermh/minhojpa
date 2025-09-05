package com.minhojpa.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 Member 클래스가 DB 테이블과 매핑된다는 뜻
@Getter
@Setter
@NoArgsConstructor
public class Member {
	
	@Id // 기본 키(Primary key) 즉, 기본 키(PK)로 사용될 필드를 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 생성하도록 설정 auto_increment처럼 자동 증가
	private Long id;
	
	@Column(nullable = false) // name은 NOT NULL로 설정 이 컬럼은 null을 허용하지 않음
	private String name;
	
	@Column(nullable = false, unique = true) // 이메일은 중복 없도록 설정
	private String email;
	
	@Column(nullable = false)
	private String password; 
	
	//회원 한 명은 여러 댓글을 작성할 수 있다
	//외래키는 Comment 엔티티의 writer 필드에 있음(주인이 아님)
	//이 Member가 저장/삭제될 때 관련 댓글도 같이 저장/삭제됨
	//이 회원이 쓴 댓글들을 리스트 형태로 저장
	//nullPointerException 방지를 위해 미리 리스트 초기화
	@OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	
}
