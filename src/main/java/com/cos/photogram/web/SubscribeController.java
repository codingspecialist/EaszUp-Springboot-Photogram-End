package com.cos.photogram.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogram.config.auth.PrincipalDetails;
import com.cos.photogram.service.SubscribeService;
import com.cos.photogram.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscribeController {

	private final SubscribeService subscribeService;
	 

	
	@PostMapping("/subscribe/{toUserId}") // /follow/3
	public CMRespDto<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId) {
		int result = subscribeService.구독하기(principalDetails.getUser().getId(), toUserId);
		return new CMRespDto<>(1, result);
	}
	
	@DeleteMapping("/subscribe/{toUserId}") // /follow/3
	public CMRespDto<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId) {
		int result = subscribeService.구독취소(principalDetails.getUser().getId(), toUserId);
		return new CMRespDto<>(1, result);
	}
}


