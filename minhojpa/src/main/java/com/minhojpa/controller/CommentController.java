package com.minhojpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.minhojpa.entity.Comment;
import com.minhojpa.entity.Member;
import com.minhojpa.entity.Post;
import com.minhojpa.service.CommentService;
import com.minhojpa.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comments")
public class CommentController {
	private final CommentService commentService;
	private final PostService postService;

	@Autowired
	public CommentController(CommentService commentService, PostService postService) {
		this.commentService = commentService;
		this.postService = postService;
	}

	// 댓글 등록
	@PostMapping("/add")
	public String addComment(@RequestParam Long postId, @RequestParam String content, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			return "redirect:/login";
		}

		Post post = postService.findById(postId);
		if (post == null) {
			return "redirect:/posts";
		}

		Comment comment = new Comment();
		comment.setContent(content);
		comment.setPost(post);
		comment.setWriter(loginMember);

		commentService.saveComment(comment);
		return "redirect:/posts/" + postId;
	}

	// 댓글 수정 폼
	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null)
			return "redirect:/login";

		Comment comment = commentService.findById(id);
		if (comment == null || !comment.getWriter().getId().equals(loginMember.getId())) {
			return "redirect:/posts";
		}

		model.addAttribute("comment", comment);
		return "editComment";
	}

	// 댓글 수정 처리
	@PostMapping("/{id}/edit")
	public String editComment(@PathVariable Long id, @RequestParam String content, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			return "redirect:/login";
		}

		Comment comment = commentService.findById(id);
		if (comment == null || !comment.getWriter().getId().equals(loginMember.getId())) {
			return "redirect:/posts";
		}
		comment.setContent(content);
		commentService.saveComment(comment); // comment 업데이트 처리
		return "redirect:/posts/" + comment.getPost().getId();
	}
	
	//댓글 삭제 처리
	@PostMapping("/{id}delete")
	public String deleteComment(@PathVariable Long id, HttpSession session) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Comment comment = commentService.findById(id);
		if(comment == null || !comment.getWriter().getId().equals(loginMember.getId())){
			return "redirect:/posts";
		}
		Long postId = comment.getPost().getId();
		commentService.deleteById(id);
		return "redirect:/posts/" + postId;
	}
	
}
