package com.minhojpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {
	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	// 게시글 목록
	@GetMapping("/posts")
	public String listPosts(Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		List<Post> posts = postService.findAll();
		model.addAttribute("posts", posts); 
		return "postList";
	}

	// 게시글 작성 폼
	@GetMapping("/posts/new")
	public String showCreateForm(Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		model.addAttribute("post", new Post()); 
		return "createPost"; 
	}

	// 게시글 작성 처리
	@PostMapping("/posts")
	public String createPost(@ModelAttribute Post post, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			return "redirect:/login";
		}
		post.setWriter(loginMember); // post 엔터티에서 바로 값 가져옴 그 안에 loginMember 넣어줌
		postService.save(post);
		return "redirect:/posts";
	}

	// 게시글 상세보기
	@GetMapping("/posts/{id}")
	public String viewPost(@PathVariable Long id, Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post = postService.findById(id);
		if (post == null) {
			return "redirect:/posts";
		}
		
		//댓글 목록
		List<Comment> comments = post.getComments(); // 양방향 관계

		model.addAttribute("post", post);
		model.addAttribute("comments", comments);
		model.addAttribute("session", session); 
		return "postDetail"; 
	}
	
	//게시글 수정 폼
	@GetMapping("/posts/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:login";
		}
		Post post = postService.findById(id);
		if(post == null) { 
			return "redirect:/posts"; 
		}
		model.addAttribute("post", post);
		return "editPost";
	}
	
	//게시글 수정 처리하기
	@PostMapping("/posts/{id}/edit")
	public String updatePost(@PathVariable Long id, @ModelAttribute Post updatedPost, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post = postService.findById(id);
		if(post == null || !post.getWriter().getId().equals(loginMember.getId())) {
			return "redirect:/posts";
			// 게시글이 없거나, 작성자가 다르면 수정 못 함
		}
		
		//수정 내용 반영
		post.setTitle(updatedPost.getTitle());
		post.setContent(updatedPost.getContent());		
		postService.save(post); // save는 수정도 됨 (JPA는 같은 ID면 merge 됨)
		return "redirect:/posts/" + id;
	}
	
	//게시글 삭제 처리하기
	@PostMapping("/posts/{id}/delete")
	public String deletePost(@PathVariable Long id, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");	
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Post post = postService.findById(id);
		if(post == null || !post.getWriter().getId().equals(loginMember.getId())) {
			return "redirect:/posts";
		}
		
		postService.deleteById(id);
		return "redirect:/posts";
	}
}