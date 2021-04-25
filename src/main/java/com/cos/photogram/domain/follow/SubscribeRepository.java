package com.cos.photogram.domain.follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{

	// write (@Modifying)
	@Modifying
	@Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	int mSubscribe(int fromUserId, int toUserId); // prepareStatement updateQuery() => -1 0 1
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	int mUnSubscribe(int fromUserId, int toUserId); // prepareStatement updateQuery() => -1 0 1
	
	@Query(value = "select count(*) from subscribe where fromUserId = :principalId AND toUserId = :userId", nativeQuery = true)
	int mSubscribeState(int principalId, int userId);
	
	@Query(value = "select count(*) from subscribe where fromUserId = :userId", nativeQuery = true)
	int mSubscribeCount(int userId);
}
