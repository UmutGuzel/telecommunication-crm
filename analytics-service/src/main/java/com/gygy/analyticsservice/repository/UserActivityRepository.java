package com.gygy.analyticsservice.repository;

import com.gygy.analyticsservice.model.UserActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for user activity data.
 */
@Repository
public interface UserActivityRepository extends MongoRepository<UserActivity, String> {

    /**
     * Find activities by user ID, ordered by timestamp (newest first).
     */
    List<UserActivity> findByUserIdOrderByTimestampDesc(String userId);

    /**
     * Find most recent 100 activities across all users.
     */
    List<UserActivity> findTop100ByOrderByTimestampDesc();
}