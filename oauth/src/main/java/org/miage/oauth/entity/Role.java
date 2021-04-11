package org.miage.oauth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_CITIZEN("ROLE_CITIZEN"),
    ROLE_CLERK("ROLE_CLERK"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ERP("ROLE_ERP"),
    ROLE_EMPLOYE("ROLE_EMPLOYE"),
    ROLE_PARTENER("ROLE_PARTENER");

    private final String value;
}
