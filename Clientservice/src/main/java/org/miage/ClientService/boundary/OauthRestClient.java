package org.miage.clientservice.boundary;

import org.miage.clientservice.entity.Oauth.UserOauth;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "oauth-service")
public interface OauthRestClient {

    @PostMapping(value = "/user")
    void createClient(@RequestBody UserOauth user);
}
