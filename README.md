# Projet Spring Master 2 ACSI
## URL Github: 
https://github.com/AnthonyPellizzeri/SpringM2.git
## Lancement de l'application :
###  Etape 1 : lancer les services de gestion (consul, bases de données) 
==> docker-compose up -d

### Etape 2 : lancer les microservices Java (service-client, service-degradation, service-oauth)

Excécuter la commande ci-dessous sur chaque service 

==> mvn clean install -DskipTests 

Puis lancer la commande:

==> docker-compose -f docker-compose.microservices.yml up -d

###  Etape 3 : utiliser les api

### Etape 4 : fermer tous les docker
==> docker-compose -f docker-compose.microservices.yml stop

==> docker-compose -f docker-compose.microservices.yml rm -fv

==> docker-compose stop

==> docker-compose rm -fv

## Authorisation:
### Récupérer le JWT
 - type : OAuth 2.0
 - Grant Type : Password Credentials
 - Access Token URL : http://localhost:9191/oauth/token
 - Client ID : api-client
 - Client Secret : password
 - Username : operrin / apellizzeri
 - Password : password
 - Scope : read, write

### Header:
- Access Token: JWT 
- Header Prefix: Bearer

## Routes disponibles par swagger:
localhost:[port]/v3/api-docs

localhost:[port]/swagger-ui.html
