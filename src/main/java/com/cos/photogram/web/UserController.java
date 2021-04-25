package com.cos.photogram.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.domain.user.User;
import com.cos.photogram.service.SubscribeService;
import com.cos.photogram.service.UserService;
import com.cos.photogram.web.dto.CMRespDto;
import com.cos.photogram.web.dto.subscribe.SubscribeRespDto;
import com.cos.photogram.web.dto.user.UserProfileRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final SubscribeService subscribeService;

	@GetMapping("/user/{pageUserId}/subscribe") // data 리턴하는 것
	public @ResponseBody CMRespDto<?> followList(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PathVariable int pageUserId) {
		List<SubscribeRespDto> subscribeRespDto = subscribeService.구독리스트(principalDetails.getUser().getId(), pageUserId);

		return new CMRespDto<>(1, subscribeRespDto);
	}

	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id, Model model,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {

		UserProfileRespDto userProfileRespDto = userService.회원프로필(id, principalDetails.getUser().getId());
		model.addAttribute("dto", userProfileRespDto);

		return "user/profile";
	}

	@GetMapping("/user/{id}/update")
	public String profileSetting(@PathVariable int id) {
		return "user/update";
	}

	@PutMapping("/user/{id}")
	public @ResponseBody CMRespDto<?> profileUpdate(@PathVariable int id, User user,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User userEntity = userService.회원수정(id, user);
		principalDetails.setUser(userEntity);
		return new CMRespDto<>(1, null);
	}

	@PutMapping("/user/{id}/profileImageUrl")
	public @ResponseBody CMRespDto<?> profileImageUrlUpdate(@PathVariable int id, MultipartFile profileImageFile,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		User userEntity = userService.회원사진변경(profileImageFile, principalDetails);
		principalDetails.setUser(userEntity);
		return new CMRespDto<>(1, null);
	}
}
