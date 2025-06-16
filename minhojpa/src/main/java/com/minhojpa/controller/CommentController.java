package com.minhojpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	//댓글 등록
	@PostMapping("/add")
	public String addComment(@RequestParam Long postId,
	                         @RequestParam String content,
	                         HttpSession session) {
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

}
