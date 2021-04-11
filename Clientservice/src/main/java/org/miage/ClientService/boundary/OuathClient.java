package org.miage.clientservice.boundary;

import lombok.AllArgsConstructor;
import org.miage.clientservice.entity.Oauth.UserOauth;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OuathClient {
    private final OauthRestClient oauthRestClient;

    public void createUser(UserOauth user) {
        //oauthRestClient.createClient();
        //oauthRestClient.createClient(user);
    }
}
