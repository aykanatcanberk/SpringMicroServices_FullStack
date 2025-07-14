package com.fitness.gateway.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/api/users/{userId}/validate")
    Boolean validateUser(@PathVariable("userId") String userId);

    @PostMapping("/api/users/register")
    UserResponse registerUser(@RequestBody RegisterRequest request);
}
