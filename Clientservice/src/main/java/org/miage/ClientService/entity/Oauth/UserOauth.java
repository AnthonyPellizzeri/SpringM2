package org.miage.clientservice.entity.Oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOauth implements Serializable {

    private String id;
    private String username;
    private String password;
}
