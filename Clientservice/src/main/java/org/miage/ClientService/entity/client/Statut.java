package org.miage.clientservice.entity.client;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Statut {
    ACTIF("Actif"),
    SUPPRIME("Supprimé");

    public final String value;
}
