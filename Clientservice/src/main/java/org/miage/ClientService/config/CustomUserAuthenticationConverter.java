package org.miage.clientservice.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        UserPrincipal userPrincipal = new UserPrincipal();
        if (map.get("id") != null) {
            userPrincipal.setId((String) map.get("id"));
        }
        if (map.get("user_name") != null) {
            userPrincipal.setUsername((String) map.get("user_name"));
        }
        return new UsernamePasswordAuthenticationToken(userPrincipal, "N/A", getAuthorities(map));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return Collections.emptySet();
        }
        Object grantedAuthorities = map.get(AUTHORITIES);
        if (grantedAuthorities instanceof String) {
            return commaSeparatedStringToAuthorityList((String) grantedAuthorities);
        }
        if (grantedAuthorities instanceof Collection) {
            return commaSeparatedStringToAuthorityList(
                    collectionToCommaDelimitedString((Collection<?>) grantedAuthorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
