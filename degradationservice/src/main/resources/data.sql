INSERT INTO degradation(id,description,name, statut) VALUES ('123','la statut du centre ville est cassée','statut centre ville','ACTIF');
INSERT INTO action(id,date,etat,statut,responsable) VALUES ('321','01-04-2021 21:32:32','DEBUT','ACTIVE','2ec4d38f-52f4-4448-80d6-2f87b53fda82d');
INSERT INTO action(id,date,etat,statut,responsable) VALUES ('4321','01-04-2021 21:32:33','ETUDE','ACTIVE','2ec4d38f-52f4-4448-80d6-2f87b53fda82d');
INSERT INTO degradation_action(degradation_id,action_id) VALUES ('123','321');
INSERT INTO degradation_action(degradation_id,action_id) VALUES ('123','4321');
