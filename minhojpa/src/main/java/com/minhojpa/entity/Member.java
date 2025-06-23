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
	
	@OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();
	
	@Id // 기본 키(Primary key) 즉, 기본 키(PK)로 사용될 필드를 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 생성하도록 설정 auto_increment처럼 자동 증가
	private Long id;
	
	@Column(nullable = false) // name은 NOT NULL로 설정 이 컬럼은 null을 허용하지 않음
	private String name;
	
	@Column(nullable = false, unique = true) // 이메일은 중복 없도록 설정
	private String email;
	
	@Column(nullable = false)
	private String password; // 비밀번호 필드 추가
	
}

/*
 DB 쿼리
 CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- PK, 자동 증가
    name VARCHAR(255) NOT NULL,               -- 이름, NOT NULL
    email VARCHAR(255) NOT NULL UNIQUE,       -- 이메일, NOT NULL + UNIQUE (중복 방지)
    password VARCHAR(255) NOT NULL            -- 비밀번호, NOT NULL
);
 */