package org.miage.degradationservice.entity.degradation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.miage.degradationservice.entity.action.Action;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Degradation {
    private static final long serialVersionUID = 5473627883709L;
    @Id
    private String id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private StatutDegradation statut;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Action> action = new ArrayList<>();
}
