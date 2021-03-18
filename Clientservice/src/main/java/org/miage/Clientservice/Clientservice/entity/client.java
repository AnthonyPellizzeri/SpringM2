package org.miage.Clientservice.Clientservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class client {
    private static final long serialVersionUID = 5473627883709L;
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String city;
    private int postalcode;
}
