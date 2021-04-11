package org.miage.degradationservice.entity.action;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatutAction {
    ACTIVE("ACTIVE"),
    TERMINEE("TERMINEE");
    public final String value;
}
