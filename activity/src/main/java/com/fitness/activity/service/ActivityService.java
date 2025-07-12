package com.fitness.activity.service;

import com.fitness.activity.client.UserClient;
import com.fitness.activity.config.RabbitMqConfig;
import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.mapper.ActivityMapper;
import com.fitness.activity.model.Activity;
import com.fitness.activity.repository.ActivityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final UserClient userClient;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {

        log.info("Validating user with ID: " + activityRequest.getUserId());

        Boolean userExists = userClient.validateUser(activityRequest.getUserId());
        if (!Boolean.TRUE.equals(userExists)) {
            throw new IllegalArgumentException("User not found: " + activityRequest.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();

        Activity saved = activityRepository.save(activity);


        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, saved);
        }catch (Exception e) {
            log.error("Error sending activity to RabbitMQ: " + e.getMessage());
        }

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
