package com.minhojpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	// 기본적인 CRUD 메서드 (save, findAll, findById 등)는 자동 제공됨
	
	Page<Post> findAll(Pageable pageable); // 기본 페이징 메서드
}
