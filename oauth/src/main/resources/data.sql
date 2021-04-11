drop table if exists oauth_client_details;
create table oauth_client_details
(
    client_id               VARCHAR(255) PRIMARY KEY,
    resource_ids            VARCHAR(255),
    client_secret           VARCHAR(255),
    scope                   VARCHAR(255),
    authorized_grant_types  VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities             VARCHAR(255),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(255)
);
insert into oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types,
                                  web_server_redirect_uri, authorities, access_token_validity,
                                  refresh_token_validity, additional_information, autoapprove)
                                  values ('api-client', 'api-resource', '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi',
        'read,write', 'password,authorization_code,refresh_token,client_credentials,implicit', null, null, 3600,
        600,
        null, null);
-- ADMIN / password
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('2ec4d38f-52f4-4448-80d6-2f87b53fda82d', 'apellizzeri',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_ADMIN',
        true, false, false, false);

insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0e1f9da7d9b3', 'operrin',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_ADMIN',
        true, false, false, false);

insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0e1f9dzda79b3', 'ADMIN',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_ADMIN',
        true, false, false, false);

-- CITIZEN
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0e1f9dzdda7973', 'CITIZEN',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_CITIZEN',
        true, false, false, false);

-- CLERK
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0dzdze1f9da7973', 'CLERK',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_CLERK',
        true, false, false, false);

-- MANAGER
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0ezzz1f9da7973', 'MANAGER',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_MANAGER',
        true, false, false, false);

-- ERP
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0evcs1f9da7973', 'ERP',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_ERP',
        true, false, false, false);

-- EMPLOYE
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0ddqze1f9da7973', 'EMPLOYE',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_EMPLOYE',
        true, false, false, false);

-- PARTENER
insert into user_account (id, username, password, role, enabled, account_locked, account_expired, credentials_expired)
values ('8db6ecba-a63b-4a38-8686-c0edqzdz1f9da7973', 'PARTENER',
        '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'ROLE_PARTENER',
        true, false, false, false);
