package com.minhojpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.entity.PostLike;
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	// 특정 회원이 특정 게시글에 좋아요 했는지 확인
	Optional<PostLike> findByMemberAndPost(Member member, Post post);
	
	Long countByPost(Post post);
	
	// 특정 회원이 누른 좋아요 전체 조회
	List<PostLike> findByMember(Member member);
}
