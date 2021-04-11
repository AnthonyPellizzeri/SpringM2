package org.miage.degradationservice.entity.degradation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatutDegradation {
    ACTIF("ACTIF"),
    INACTIF("INACTIF");
    public final String value;
}
