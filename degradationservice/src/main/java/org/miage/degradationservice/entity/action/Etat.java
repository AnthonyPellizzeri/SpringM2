package org.miage.degradationservice.entity.action;

import lombok.AllArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
public enum Etat {
    DEBUT("DEBUT"),
    ETUDE("ETUDE"),
    ETUDE_DETAILLEE("ETUDE_DETAILLEE"),
    APPROUVEE("APPROUVEE"),
    REJET("REJET");

    public final String value;
}
