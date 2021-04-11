package org.miage.degradationservice.entity.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionInput {
    @NotNull
    @NotBlank
    private Etat etat;
    @NotNull
    @NotBlank
    private StatutAction Status;
    @NotNull
    @NotBlank
    private String responsable;
    @NotNull
    @NotBlank
    private LocalDateTime date;
}
