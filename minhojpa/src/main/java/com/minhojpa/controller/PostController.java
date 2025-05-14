package com.minhojpa.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.service.MemberService;
import com.minhojpa.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	private final PostService postService;
	private final MemberService memberService;
	
	@Autowired
	public PostController(PostService postService, MemberService memberService) {
		this.postService = postService;
		this.memberService = memberService;
	}
	
	//게시글 목록
	@GetMapping("/posts")
	public String listPosts(Model model) {
		List<Post> posts = postService.finaAll();
		model.addAttribute("post", posts);
		return "postList"; // templates/postList.html
	}
	
	//게시글 작성 폼
	@GetMapping("/posts/new")
	public String showCreateForm(Model model) {
		model.addAttribute("post", new Post());
		return "createPost";  // templates/createPost.html
	}
	
	//게시글 작성 처리
	@PostMapping("/posts")
	public String createPost(@ModelAttribute Post post, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		post.setWriter(loginMember);
		postService.save(post);
		return "redirect:/posts";
	}
	
	//게시글 상세보기
	@GetMapping("/posts/{id}")
	public String viewPost(@PathVariable Long id, Model model) {
		Post post = postService.findById(id);
		if(post == null) {
			return "redirect:/posts";
		}
		model.addAttribute("post", post);
		return "postDetail"; // templates/postDetail.html
}
}