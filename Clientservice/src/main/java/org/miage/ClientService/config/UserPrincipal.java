package org.miage.clientservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements Principal {

    private String id;
    private String username;

    @Override
    public String getName() {
        return this.username;
    }
}
