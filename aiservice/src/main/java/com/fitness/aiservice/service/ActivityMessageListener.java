package com.fitness.aiservice.service;


import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final RecommendationRepository recommendationRepository;
    private final ActivityAIService activityAIService;

    @RabbitListener(queues = "activity.queue" )
    public void processActivity(Activity activity) {
        //log.info("processing activity {}", activity.getId());
        //log.info("Generated recommendation: {}", activityAIService.generateRecommendation(activity));

        Recommendation recommendation =  activityAIService.generateRecommendation(activity);

        log.info("Generated recommendation: {}", recommendation);
        recommendationRepository.save(recommendation);
    }
}
