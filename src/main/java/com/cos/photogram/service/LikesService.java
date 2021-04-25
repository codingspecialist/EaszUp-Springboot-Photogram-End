package com.cos.photogram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogram.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikesService {

	private final LikesRepository likesRepository;
	
	@Transactional
	public void 좋아요(int imageId, int principalId) {
		likesRepository.mLike(imageId, principalId);
	}
	
	@Transactional
	public void 싫어요(int imageId, int principalId) {
		likesRepository.mUnLike(imageId, principalId);
	}
}
