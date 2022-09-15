package com.ciandt.ExceptionsMyMusic.application.client;

import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "tokenFindClient", url = "http://localhost:8080")
public interface MyFeignClient {
    @PostMapping("/api/v1/token")
    public String clientUserId(TokenDataDTO id);

    @PostMapping("/api/v1/token/authorizer")
    public String clientValidator(TokenDataDTO tokenDataDTO);
}
