package com.cos.photogram.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.comment.Comment;
import com.cos.photogram.domain.image.Image;
import com.cos.photogram.service.CommentService;
import com.cos.photogram.service.ImageService;
import com.cos.photogram.service.LikesService;
import com.cos.photogram.web.dto.CMRespDto;
import com.cos.photogram.web.dto.image.ImageReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	private final LikesService likesService;
	private final CommentService commentService;
	
	@GetMapping({"/", "/image/story"})
	public String feed() {
		return "image/story";
	}
	
	//  주소 : /image?page=0
	@GetMapping("/image")
	public @ResponseBody CMRespDto<?> image(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, 
			@PageableDefault(size=3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		
		Page<Image> pages = imageService.피드이미지(principalDetails.getUser().getId(), pageable);
		return new CMRespDto<>(1, pages); // MessageConverter 발동 = Jackson = 무한참조
	}
	
	
	
	@GetMapping("/image/popular")
	public String explore(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		model.addAttribute("images", imageService.인기사진(principalDetails.getUser().getId()));
		
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String image(ImageReqDto imageReqDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		imageService.사진업로드(imageReqDto, principalDetails);
		
		return "redirect:/user/"+principalDetails.getUser().getId();
	}
	
	@PostMapping("/image/{imageId}/likes")
	public @ResponseBody CMRespDto<?> like(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int imageId){
		likesService.좋아요(imageId, principalDetails.getUser().getId());
		return new CMRespDto<>(1, null);
	}
	
	@DeleteMapping("/image/{imageId}/likes")
	public @ResponseBody CMRespDto<?> unLike(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int imageId){
		likesService.싫어요(imageId, principalDetails.getUser().getId());
		return new CMRespDto<>(1, null);
	}
	
	@PostMapping("/image/{imageId}/comment")
	public @ResponseBody CMRespDto<?> save(@PathVariable int imageId, @RequestBody String content, @AuthenticationPrincipal PrincipalDetails principalDetails){   // content, imageId, userId(세션)
		Comment commentEntity = commentService.댓글쓰기(principalDetails.getUser(), content, imageId);
		
		return new CMRespDto<>(1, commentEntity);
	}
}













