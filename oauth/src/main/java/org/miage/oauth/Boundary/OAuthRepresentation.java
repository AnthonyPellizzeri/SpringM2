package org.miage.oauth.Boundary;

import lombok.AllArgsConstructor;
import org.miage.oauth.entity.User;
import org.miage.oauth.entity.UserInput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static org.miage.oauth.entity.Role.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
public class OAuthRepresentation {

    private final UserResource resource;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public Principal principal(Principal principal) {
        return principal;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserInput userInput) {
        Optional<User> user = resource.findById(userInput.getId());
        if (user.isEmpty()) {
            User u = new User();
            u.setId(userInput.getId());
            u.setUsername(userInput.getUsername());
            u.setPassword(passwordEncoder.encode(userInput.getPassword()));
            u.setEnabled(true);
            u.setCredentialsNonExpired(true);
            u.setAccountNonLocked(true);
            u.setAccountNonExpired(true);
            u.setRole(ROLE_CITIZEN);
            resource.save(u);
        }
        return ResponseEntity.ok().build();
    }
}
