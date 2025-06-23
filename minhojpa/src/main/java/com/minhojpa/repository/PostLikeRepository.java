package com.minhojpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.entity.PostLike;
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Optional<PostLike> findByMemberAndPost(Member member, Post post);
	Long countByPost(Post post);
}
