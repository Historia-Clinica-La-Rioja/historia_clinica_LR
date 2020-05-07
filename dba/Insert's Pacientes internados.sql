
---------- PERSON ----------
INSERT INTO public.person (id,first_name, middle_names, last_name, other_last_names, identification_type_id, identification_number, gender_id, birth_date) 
VALUES(61,'Alejandro','Martin','Perez',NULL,1,'36000001',2,'1990-01-02');

INSERT INTO public.person (id,first_name, middle_names, last_name, other_last_names, identification_type_id, identification_number, gender_id, birth_date) 
VALUES(62,'María','Soledad','Rinaldi',NULL,1,'37000001',1,'1991-01-03');

INSERT INTO public.person (id,first_name, middle_names, last_name, other_last_names, identification_type_id, identification_number, gender_id, birth_date) 
VALUES(63,'Veronica','Analía','Prado','Rojas',1,'35000001',1,'1992-01-02');

INSERT INTO public.person (id,first_name, middle_names, last_name, other_last_names, identification_type_id, identification_number, gender_id, birth_date) 
VALUES(64,'Alejo','Fernando','Villar','Frade',1,'34000001',2,'1985-01-01');

select setval('person_id_seq', (select max(id) from person));

---------- ADDRESS ----------
INSERT INTO public.address (id, street, number, floor, apartment, quarter, city_id, postcode) 
VALUES (63,'Maritorena','16',NULL,'A','calvario',3687,'7000');
​
INSERT INTO public.address (id, street, number, floor, apartment, quarter, city_id, postcode) 
VALUES (64,'Alem','420',NULL,NULL,'centro',3687,'7000');

INSERT INTO public.address (id, street, number, floor, apartment, quarter, city_id, postcode) 
VALUES (65,'Av. Alvear','125',NULL,'2','Dique',3687,'7000');
​
INSERT INTO public.address (id, street, number, floor, apartment, quarter, city_id, postcode) 
VALUES (66,'Pasteur','728',NULL,NULL,'villa italia',3687,'7000');

select setval('address_id_seq', (select max(id) from address));


---------- PERSON EXTENDED ----------

INSERT INTO public.person_extended (person_id,cuil,mothers_last_name,address_id,phone_number,email,ethnic,religion,name_self_determination,gender_self_determination) 
VALUES (61,'20360000011',NULL,63,'2494603932','mperez@gmail.com',NULL,'cristianismo','Martin',2);
​
INSERT INTO public.person_extended (person_id,cuil,mothers_last_name,address_id,phone_number,email,ethnic,religion,name_self_determination,gender_self_determination) 
VALUES (62,'27370000011',NULL,64,'2494603932','msoledadrinaldi@gmail.com',NULL,'cristianismo','María Soledad',1);

INSERT INTO public.person_extended (person_id,cuil,mothers_last_name,address_id,phone_number,email,ethnic,religion,name_self_determination,gender_self_determination) 
VALUES (63,'27350000011','Rojas',65,'2494603932','vaprado@gmail.com',NULL,'cristianismo','Verónica Analía',1);
​
INSERT INTO public.person_extended (person_id,cuil,mothers_last_name,address_id,phone_number,email,ethnic,religion,name_self_determination,gender_self_determination) 
VALUES (64,'20340000011','Frade',66,'2494603932','alejofvillar@gmail.com',NULL,'cristianismo','Alejo Fernando',2);


---------- PATIENT ----------

INSERT INTO public.patient (id,person_id,type_id,possible_duplicate,national_id,identity_verification_status_id,comments) 
VALUES (61,61,3,false,NULL,NULL,NULL);

INSERT INTO public.patient (id,person_id,type_id,possible_duplicate,national_id,identity_verification_status_id,comments) 
VALUES (62,62,3,false,NULL,NULL,NULL);

INSERT INTO public.patient (id,person_id,type_id,possible_duplicate,national_id,identity_verification_status_id,comments) 
VALUES (63,63,3,false,NULL,NULL,NULL);

INSERT INTO public.patient (id,person_id,type_id,possible_duplicate,national_id,identity_verification_status_id,comments) 
VALUES (64,64,3,false,NULL,NULL,NULL);
​
select setval('patient_id_seq', (select max(id) from patient));

---------- INSTITUTION ----------

INSERT INTO public.institution (id,name,address_id,website,phone_number,email,cuit,sisa_code) 
VALUES (10,'Institution Hardcode',1,'www.website.com','2494440000','email@email.com','30987654321','sisacode');
​
--select setval('institution_id_seq', (select max(id) from institution));

---------- BED ----------

INSERT INTO public.bed (id, room_id,bed_category_id,bed_number) values (5,5,2,'6');
 
select setval('bed_id_seq', (select max(id) from bed));

​---------- INTERNMENT EPISODE ----------

INSERT INTO public.internment_episode (patient_id,bed_id,clinical_specialty_id,status_id,note_id,anamnesis_doc_id,epicrisis_doc_id,institution_id,entry_date,discharge_date,created_by,updated_by,created_on,updated_on) 
VALUES (61,2,112,1,NULL,NULL,NULL,10,'2020-05-06','2020-05-06',-1,-1,'2020-05-06 00:00:00','2020-05-06 00:00:00');

INSERT INTO public.internment_episode (patient_id,bed_id,clinical_specialty_id,status_id,note_id,anamnesis_doc_id,epicrisis_doc_id,institution_id,entry_date,discharge_date,created_by,updated_by,created_on,updated_on) 
VALUES (62,3,112,1,NULL,NULL,NULL,10,'2020-05-06','2020-05-06',-1,-1,'2020-05-06 00:00:00','2020-05-06 00:00:00');

INSERT INTO public.internment_episode (patient_id,bed_id,clinical_specialty_id,status_id,note_id,anamnesis_doc_id,epicrisis_doc_id,institution_id,entry_date,discharge_date,created_by,updated_by,created_on,updated_on) 
VALUES (63,4,112,1,NULL,NULL,NULL,10,'2020-05-06','2020-05-06',-1,-1,'2020-05-06 00:00:00','2020-05-06 00:00:00');

INSERT INTO public.internment_episode (patient_id,bed_id,clinical_specialty_id,status_id,note_id,anamnesis_doc_id,epicrisis_doc_id,institution_id,entry_date,discharge_date,created_by,updated_by,created_on,updated_on) 
VALUES (64,5,112,1,NULL,NULL,NULL,10,'2020-05-06','2020-05-06',-1,-1,'2020-05-06 00:00:00','2020-05-06 00:00:00');

select setval('internment_episode_id_seq', (select max(id) from internment_episode ie));


INSERT INTO healthcare_professional_group(healthcare_professional_id, internment_episode_id, responsible)
VALUES (11, 1,true)
INSERT INTO healthcare_professional_group(healthcare_professional_id, internment_episode_id, responsible)
VALUES (11, 2,true)
INSERT INTO healthcare_professional_group(healthcare_professional_id, internment_episode_id, responsible)
VALUES (11, 3,true)
INSERT INTO healthcare_professional_group(healthcare_professional_id, internment_episode_id, responsible)
VALUES (11, 4,true)