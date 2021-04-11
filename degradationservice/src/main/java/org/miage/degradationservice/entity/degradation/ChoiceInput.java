package org.miage.degradationservice.entity.degradation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceInput {
    @NotNull
    @NotBlank
    private String choice;
}
