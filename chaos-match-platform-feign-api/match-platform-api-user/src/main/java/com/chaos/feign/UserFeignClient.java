package com.chaos.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "UserService")
public interface UserFeignClient {

}
