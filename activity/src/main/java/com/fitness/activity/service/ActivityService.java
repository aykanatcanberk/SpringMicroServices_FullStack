package com.fitness.activity.service;

import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.mapper.ActivityMapper;
import com.fitness.activity.model.Activity;
import com.fitness.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

        Activity saved = activityRepository.save(activity);
        return activityMapper.toResponse(saved);

    }

    public List<ActivityResponse> getUserActivity(String userId) {

        List<ActivityResponse> activities = activityRepository.findByUserId(userId).stream()
                .map(activityMapper::toResponse)
                .toList();

        return activities;
    }

    public ActivityResponse getActivityById(String activityId) {

        return activityRepository.findById(activityId)
                .map(activityMapper :: toResponse)
                .orElseThrow(()-> new RuntimeException("Activity not found with id: " + activityId));
    }
}
