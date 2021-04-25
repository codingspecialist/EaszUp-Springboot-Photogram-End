package com.cos.photogram.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogram.domain.follow.SubscribeRepository;
import com.cos.photogram.web.dto.subscribe.SubscribeRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em;
	
	@Transactional(readOnly = true)
	public List<SubscribeRespDto> 구독리스트(int principalId, int pageUserId){
		
		StringBuffer sb = new StringBuffer();
		sb.append("select u.id userId, u.username, u.profileImageUrl,  ");
		sb.append("if( (select true from subscribe where fromUserId = ? and toUserId = u.id), true, false) subscribeState, ");  // principalDetails.user.id
		sb.append("if(u.id = ?, true, false) equalState "); // principalDetails.user.id
		sb.append("from subscribe f inner join user u on u.id = f.toUserId ");
		sb.append("where f.fromUserId = ? "); // pageUserId
		
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		System.out.println("쿼리 : "+query.getResultList().get(0));
		
		JpaResultMapper result  = new JpaResultMapper();
		List<SubscribeRespDto> subscribeRespDtos = result.list(query, SubscribeRespDto.class);
		return subscribeRespDtos;
	}
	
	@Transactional
	public int 구독하기(int fromUserId, int toUserId) {
		return subscribeRepository.mSubscribe(fromUserId, toUserId);
	}
	
	@Transactional
	public int 구독취소(int fromUserId, int toUserId) {
		return subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
