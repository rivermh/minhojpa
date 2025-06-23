package com.minhojpa.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.entity.PostLike;
import com.minhojpa.repository.PostLikeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostLikeService {
	private final PostLikeRepository postLikeRepository;
	
	
	@Transactional
	public boolean toggleLike(Member member, Post post) {
		Optional<PostLike> optionalLike = postLikeRepository.findByMemberAndPost(member, post);
		
		if(optionalLike.isPresent()) {
			postLikeRepository.delete(optionalLike.get());
			return false; // 좋아요 취소
		}else {
			PostLike postLike = new PostLike(member, post);
			postLikeRepository.save(postLike);
			return true;
		}
	}
	public boolean isLikedByMember(Member member, Post post) {
		return postLikeRepository.findByMemberAndPost(member, post).isPresent();
	}
	
	public long countLikes(Post post) {
		return postLikeRepository.countByPost(post);
	}
	
	public List<Post> getLikedPosts(Member member){
		List<PostLike> likes = postLikeRepository.findByMember(member);
		List<Post> likedPosts = new ArrayList<>();
	    
	    for (PostLike like : likes) {
	        likedPosts.add(like.getPost());
	    } 
	    
	    return likedPosts;
	}
}
