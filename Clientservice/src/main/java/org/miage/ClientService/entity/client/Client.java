package org.miage.clientservice.entity.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Client implements Serializable {
    private static final long serialVersionUID = 5473627883709L;
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String city;
    private int postalcode;
    @Enumerated(EnumType.STRING)
    private Statut statut;
}
