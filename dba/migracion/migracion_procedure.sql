create procedure migrate(offset_value integer)
    language plpgsql
as
$$
DECLARE
temprow RECORD;
  temprow2 RECORD;
  temprow3 RECORD;
BEGIN

    -- Direcciones
INSERT INTO migrate.address (id, street, number, floor, apartment, quarter, city_id, postcode, latitude, longitude)
SELECT (-offset_value-a.id), a.street, a.number, a.floor, a.apartment, a.quarter, a.city_id,
       a.postcode, a.latitude, a.longitude
FROM public.address AS a;

--------------------------------------------------------------------------------------------------------------------
-- Personas
INSERT INTO migrate.person (id, first_name, middle_names, last_name, other_last_names, identification_type_id,
                            identification_number, gender_id, birth_date)
SELECT (-offset_value-p.id), p.first_name, p.middle_names, p.last_name, p.other_last_names,
       p.identification_type_id, p.identification_number, p.gender_id, p.birth_date
FROM public.person AS p;


INSERT INTO migrate.person_extended (person_id, cuil, mothers_last_name, address_id, phone_number, email, religion,
                                     name_self_determination, gender_self_determination, photo_file_path, ethnicity_id,
                                     education_level_id, occupation_id, other_gender_self_determination, phone_prefix)
SELECT (-offset_value-pe.person_id), pe.cuil, pe.mothers_last_name, (-offset_value-pe.address_id),
       pe.phone_number, pe.email, pe.religion, pe.name_self_determination, pe.gender_self_determination,
       pe.photo_file_path, pe.ethnicity_id, pe.education_level_id, pe.occupation_id,
       pe.other_gender_self_determination, pe.phone_prefix
FROM person_extended AS pe;

INSERT INTO migrate.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                             comments)
SELECT (-offset_value-pa.id), (-offset_value-pa.person_id), pa.type_id, pa.possible_duplicate,
       pa.national_id, pa.identity_verification_status_id, pa.comments
FROM public.patient AS pa;
--------------------------------------------------------------------------------------------------------------------
-- Usuarios
INSERT INTO migrate.users (id, created_on, deleted_on, deleted, updated_on, last_login, enable, username, created_by,
                           updated_by, deleted_by)
SELECT (-offset_value-u.id), u.created_on, u.deleted_on, u.deleted, u.updated_on, u.last_login, u.enable,
       ('MIGRACION' || u.username), (-offset_value-u.created_by), (-offset_value-u.updated_by), (-offset_value-u.deleted_by)
FROM public.users AS u
WHERE u.created_by != -1;

INSERT INTO migrate.user_password (id, created_on, deleted_on, deleted, updated_on, hash_algorithm, password, salt,
                                   created_by, updated_by, deleted_by)
SELECT (-offset_value-up.id), up.created_on, up.deleted_on, up.deleted, up.updated_on, up.hash_algorithm, up.password,
       up.salt, (-offset_value-up.created_by), (-offset_value-up.updated_by),(-offset_value-up.deleted_by)
FROM public.users AS u
         JOIN public.user_password AS up ON u.id = up.id
WHERE u.created_by != -1;

--------------------------------------------------------------------------------------------------------------------
-- Roles
INSERT INTO migrate.user_role (role_id, user_id, created_on, deleted_on, deleted, updated_on, created_by, updated_by,
                               institution_id, deleted_by)
SELECT ur.role_id, (-offset_value-ur.user_id), ur.created_on, ur.deleted_on, ur.deleted, ur.updated_on,
       (-offset_value-ur.created_by), (-offset_value-ur.updated_by), (-offset_value-ur.institution_id),
       (-offset_value-ur.deleted_by)
FROM public.user_role AS ur
         JOIN public.users AS u ON u.id = ur.user_id
WHERE u.created_by != -1;

--------------------------------------------------------------------------------------------------------------------
-- Keys

-- Aca habría que ver porque hay UQ sobre el campo key, y por ahí habría que ajustar y hacer la UQ sobre los dos campos (user_id, key)
--SELECT INTO migrate.user_key (user_id, "key")
--VALUES ((-offset_value-temprow.uk_user_id), temprow.key)
--FROM public.user_key AS uk
--JOIN public.users AS u ON u.id = uk.user_id
--WHERE u.created_by != -1

--------------------------------------------------------------------------------------------------------------------
-- Institution
INSERT INTO migrate.institution (id, name, address_id, website, phone_number, email, cuit, sisa_code, timezone,
                                 dependency_id, province_code)
SELECT (-offset_value-i.id), i.name, (-offset_value-i.address_id), i.website, i.phone_number,
       i.email, i.cuit, i.sisa_code, i.timezone, i.dependency_id, i.province_code
FROM public.institution AS i;

--------------------------------------------------------------------------------------------------------------------
-- Sector
INSERT INTO migrate.sector (id, institution_id, description, sector_type_id, sector_organization_id, age_group_id,
                            care_type_id, hospitalization_type_id, sector_id)
SELECT (-offset_value-s.id), (-offset_value-s.institution_id), s.description,
       s.sector_type_id, s.sector_organization_id, s.age_group_id, s.care_type_id,
       s.hospitalization_type_id, (-offset_value-s.sector_id)
FROM public.sector AS s;

--------------------------------------------------------------------------------------------------------------------
-- Clinical specialty Sector
INSERT INTO migrate.clinical_specialty_sector (id, description, clinical_specialty_id, sector_id)
SELECT (-offset_value-css.id), css.description, css.clinical_specialty_id, (-offset_value-css.sector_id)
FROM public.clinical_specialty_sector AS css;

--------------------------------------------------------------------------------------------------------------------
-- Rooms
INSERT INTO migrate.room (id, clinical_specialty_sector_id, description, type, discharge_date, room_number, sector_id)
SELECT (-offset_value-r.id), (-offset_value-r.clinical_specialty_sector_id), r.description, r.type,
       r.discharge_date, r.room_number, (-offset_value-r.sector_id)
FROM public.room AS r;

--------------------------------------------------------------------------------------------------------------------
-- Bed
INSERT INTO migrate.bed (id, room_id, bed_category_id, bed_number, enabled, available, free)
SELECT (-offset_value-b.id),(-offset_value-b.room_id), b.bed_category_id, b.bed_number,
       b.enabled, b.available, b.free
FROM public.bed AS b;

--------------------------------------------------------------------------------------------------------------------
-- Profesionales
INSERT INTO migrate.healthcare_professional (id, license_number, person_id, deleted, deleted_by, deleted_on, created_on,
                                             updated_on, created_by, updated_by)
SELECT (-offset_value-hp.id), hp.license_number, (-offset_value-hp.person_id), hp.deleted,
       (-offset_value-hp.deleted_by), hp.deleted_on, hp.created_on, hp.updated_on,
       (-offset_value-hp.created_by), (-offset_value-hp.updated_by)
FROM public.healthcare_professional AS hp;

--------------------------------------------------------------------------------------------------------------------
-- Profesionales especialidades
INSERT INTO migrate.healthcare_professional_specialty (id, healthcare_professional_id, professional_specialty_id,
                                                       clinical_specialty_id, deleted, deleted_by, deleted_on,
                                                       created_on, updated_on, created_by, updated_by)
SELECT (-offset_value-hps.id), (-offset_value-hps.healthcare_professional_id), professional_specialty_id,
       hps.clinical_specialty_id, hps.deleted, (-offset_value-hps.deleted_by), hps.deleted_on,
       hps.created_on, hps.updated_on, (-offset_value-hps.created_by), (-offset_value-hps.updated_by)
FROM  public.healthcare_professional_specialty AS hps;


CREATE TEMPORARY TABLE temp_medicalcoverage_ids (id int not null) on commit drop;
    --------------------------------------------------------------------------------------------------------------------
    -- Prepagas
FOR temprow IN
SELECT mc.id AS id, name, (-offset_value-created_by) AS created_by, created_on,
       (-offset_value-updated_by) AS updated_by,
       updated_on, deleted, (-offset_value-deleted_by) AS deleted_by, deleted_on,
       CASE WHEN cuit IS NULL THEN NULL ELSE CONCAT('-',cuit) end AS cuit,
       phi.plan
FROM public.medical_coverage AS mc
         JOIN public.private_health_insurance AS phi ON mc.id = phi.id
    LOOP
INSERT INTO migrate.medical_coverage (id, name, created_by, created_on, updated_by, updated_on, deleted, deleted_by,
                                      deleted_on, cuit)
VALUES ((-offset_value-temprow.id), temprow.name, temprow.created_by, temprow.created_on, temprow.updated_by,
    temprow.updated_on, temprow.deleted, temprow.deleted_by, temprow.deleted_on, temprow.cuit);

INSERT INTO migrate.private_health_insurance (id, plan)
VALUES ((-offset_value-temprow.id), temprow.plan);

INSERT INTO temp_medicalcoverage_ids (id)
VALUES (temprow.id);

FOR temprow2 IN
SELECT phip.id AS id, phip.plan, phip.private_health_insurance_id
FROM public.private_health_insurance_plan AS phip
WHERE private_health_insurance_id = temprow.id
    LOOP
INSERT INTO migrate.private_health_insurance_plan (id, private_health_insurance_id, plan)
VALUES ((-offset_value-temprow2.id), (-offset_value-temprow2.private_health_insurance_id), temprow2.plan);
END LOOP;
END LOOP;

FOR temprow3 IN
SELECT phid.id AS id, start_date, end_date,
       CASE WHEN phid.private_health_insurance_plan_id IS NULL
                THEN NULL
            ELSE (-offset_value-phid.private_health_insurance_plan_id)
           END AS private_health_insurance_plan_id
FROM public.private_health_insurance_details AS phid
     --- WHERE phid.private_health_insurance_plan_id = temprow2.id
     --- OR phid.private_health_insurance_plan_id IS NULL
    LOOP
       -- RAISE NOTICE 'private_health_insurance_details % and %', temprow3.id, temprow3.private_health_insurance_plan_id;
INSERT INTO migrate.private_health_insurance_details (id, start_date, end_date, private_health_insurance_plan_id)
VALUES ((-offset_value-temprow3.id), temprow3.start_date, temprow3.end_date,
    temprow3.private_health_insurance_plan_id);
END LOOP;
    --------------------------------------------------------------------------------------------------------------------
    -- Obras sociales
FOR temprow IN
SELECT (-offset_value-mc.id) AS id, name, (-offset_value-created_by) AS created_by, created_on,
       (-offset_value-updated_by) AS updated_by,
       updated_on, deleted, (-offset_value-deleted_by) AS deleted_by, deleted_on, cuit, rnos, acronym
FROM public.medical_coverage AS mc
         JOIN public.health_insurance AS hi ON mc.id = hi.id
WHERE UPPER(name) NOT IN (SELECT UPPER(name) FROM migrate.medical_coverage AS mcc
                                                      JOIN migrate.health_insurance AS hii ON mcc.id = hii.id)
    LOOP
INSERT INTO migrate.medical_coverage (id, name, created_by, created_on, updated_by, updated_on, deleted, deleted_by,
                                      deleted_on, cuit)
VALUES (temprow.id, temprow.name, temprow.created_by, temprow.created_on, temprow.updated_by,
    temprow.updated_on, temprow.deleted, temprow.deleted_by, temprow.deleted_on, temprow.cuit);

INSERT INTO migrate.health_insurance (id, rnos, acronym)
VALUES (temprow.id, temprow.rnos, acronym);
END LOOP;

    --------------------------------------------------------------------------------------------------------------------
    -- Cobertura médica de paciente

    RAISE NOTICE 'patient_medical_coverage ';
FOR temprow IN
SELECT pmc.id AS id, (-offset_value-patient_id) AS patient_id,
       (-offset_value-pmc.medical_coverage_id) AS medical_coverage_id, active, vigency_date,
       affiliate_number,
       pmc.private_health_insurance_details_id AS private_health_insurance_details_id
FROM public.patient_medical_coverage AS pmc
         JOIN public.medical_coverage AS mc ON pmc.medical_coverage_id = mc.id
         JOIN public.private_health_insurance AS phi ON mc.id = phi.id
    LOOP
        --RAISE NOTICE 'patient_medical_coverage % and %', temprow.id, temprow.private_health_insurance_details_id;
INSERT INTO migrate.patient_medical_coverage (id, patient_id, medical_coverage_id, active, vigency_date,
                                              affiliate_number, private_health_insurance_details_id)
VALUES ((-offset_value-temprow.id), temprow.patient_id,
    temprow.medical_coverage_id, temprow.active, temprow.vigency_date, temprow.affiliate_number,
    (-offset_value-temprow.private_health_insurance_details_id));
END LOOP;

FOR temprow IN
SELECT pmc.id AS id, (-offset_value-patient_id) AS patient_id,
       pmc.medical_coverage_id AS medical_coverage_id, active, vigency_date,
       affiliate_number,
       pmc.private_health_insurance_details_id AS private_health_insurance_details_id
FROM public.patient_medical_coverage AS pmc
         JOIN public.medical_coverage AS mc ON pmc.medical_coverage_id = mc.id
         JOIN public.health_insurance AS hi ON mc.id = hi.id
    LOOP
        --RAISE NOTICE 'patient_medical_coverage % and %', temprow.id, temprow.private_health_insurance_details_id;
INSERT INTO migrate.patient_medical_coverage (id, patient_id, medical_coverage_id, active, vigency_date,
                                              affiliate_number, private_health_insurance_details_id)
VALUES ((-offset_value-temprow.id), temprow.patient_id,
    temprow.medical_coverage_id, temprow.active, temprow.vigency_date, temprow.affiliate_number,
    (-offset_value-temprow.private_health_insurance_details_id));
END LOOP;

    --------------------------------------------------------------------------------------------------------------------
    -- Notas
INSERT INTO migrate.note (id, description)
SELECT (-offset_value-id), description
FROM public.note;

--------------------------------------------------------------------------------------------------------------------
-- Documentos
INSERT INTO migrate.document (id, source_id, other_note_id, status_id, type_id, created_by, updated_by, created_on,
                              updated_on, physical_exam_note_id, studies_summary_note_id, evolution_note_id,
                              clinical_impression_note_id, current_illness_note_id, indications_note_id, source_type_id,
                              deleted, deleted_by, deleted_on)
SELECT (-offset_value-id), source_id, (-offset_value-other_note_id), status_id, type_id, (-offset_value-created_by),
       (-offset_value-updated_by), created_on, updated_on, (-offset_value-physical_exam_note_id),
       (-offset_value-studies_summary_note_id), (-offset_value-evolution_note_id), (-offset_value-clinical_impression_note_id),
       (-offset_value-current_illness_note_id), (-offset_value-indications_note_id), (-offset_value-source_type_id),
       deleted, (-offset_value-deleted_by), deleted_on
FROM  public.document;


INSERT INTO migrate.document_file (id, source_id, type_id, file_path, file_name, created_by, created_on, updated_by,
                                   updated_on, uuid_file, source_type_id, checksum, deleted, deleted_by, deleted_on)
SELECT (-offset_value-id), source_id, type_id, file_path, file_name, (-offset_value-created_by), created_on,
       (-offset_value-updated_by), updated_on, uuid_file, source_type_id, checksum, deleted,
       (-offset_value-deleted_by), deleted_on
FROM public.document_file;


--------------------------------------------------------------------------------------------------------------------
-- Internaciones
INSERT INTO migrate.internment_episode (id, patient_id, bed_id, clinical_specialty_id, status_id, note_id,
                                        anamnesis_doc_id, epicrisis_doc_id, entry_date, created_by, updated_by,
                                        created_on, updated_on, institution_id, probable_discharge_date, deleted,
                                        deleted_by, deleted_on, patient_medical_coverage_id)
SELECT (-offset_value-id), (-offset_value-patient_id), (-offset_value-bed_id), clinical_specialty_id, status_id,
       (-offset_value-note_id), (-offset_value-anamnesis_doc_id), (-offset_value-epicrisis_doc_id), entry_date,
       (-offset_value-created_by), (-offset_value-updated_by), created_on, updated_on, (-offset_value-institution_id),
       probable_discharge_date, deleted, (-offset_value-deleted_by), deleted_on, (-offset_value-patient_medical_coverage_id)
FROM  public.internment_episode;

INSERT INTO migrate.evolution_note_document (document_id, internment_episode_id)
SELECT (-offset_value-document_id), (-offset_value-internment_episode_id)
FROM  public.evolution_note_document;

INSERT INTO migrate.patient_discharge (internment_episode_id, administrative_discharge_date, discharge_type_id,
                                       created_by, updated_by, created_on, updated_on, medical_discharge_date, deleted,
                                       deleted_by, deleted_on)
SELECT (-offset_value-internment_episode_id), administrative_discharge_date, discharge_type_id, (-offset_value-created_by),
       (-offset_value-updated_by), created_on, updated_on, medical_discharge_date, deleted, (-offset_value-deleted_by), deleted_on
FROM  public.patient_discharge;

INSERT INTO migrate.healthcare_professional_group (healthcare_professional_id, internment_episode_id, responsible)
SELECT (-offset_value-healthcare_professional_id), (-offset_value-internment_episode_id), responsible
FROM  public.healthcare_professional_group;

--------------------------------------------------------------------------------------------------------------------
-- Consultas ambulatorias
INSERT INTO migrate.outpatient_consultation (id, patient_id, clinical_specialty_id, institution_id, start_date,
                                             document_id, doctor_id, billable, created_by, created_on, updated_by,
                                             updated_on, deleted, deleted_by, deleted_on, patient_medical_coverage_id)
SELECT (-offset_value-id), (-offset_value-patient_id), clinical_specialty_id, (-offset_value-institution_id), start_date,
       (-offset_value-document_id), (-offset_value-doctor_id), billable, (-offset_value-created_by), created_on,
       (-offset_value-updated_by), updated_on, deleted, (-offset_value-deleted_by), deleted_on, (-offset_value-patient_medical_coverage_id)
FROM  public.outpatient_consultation;

INSERT INTO migrate.outpatient_consultation_reasons (reason_id, outpatient_consultation_id)
SELECT reason_id, (-offset_value-outpatient_consultation_id)
FROM  public.outpatient_consultation_reasons;

--------------------------------------------------------------------------------------------------------------------
-- Vacunación
INSERT INTO migrate.vaccine_consultation (id, patient_id, clinical_specialty_id, institution_id, performed_date,
                                          doctor_id, billable, created_by, created_on, updated_by, updated_on, deleted,
                                          deleted_by, deleted_on, patient_medical_coverage_id)
SELECT (-offset_value-id), (-offset_value-patient_id), clinical_specialty_id, (-offset_value-institution_id), performed_date,
       (-offset_value-doctor_id), billable, (-offset_value-created_by), created_on,
       (-offset_value-updated_by), updated_on, deleted, (-offset_value-deleted_by), deleted_on,
       (-offset_value-patient_medical_coverage_id)
FROM  public.vaccine_consultation;

--------------------------------------------------------------------------------------------------------------------
-- Enfermeria
INSERT INTO migrate.nursing_consultation (id, patient_id, clinical_specialty_id, institution_id, performed_date,
                                          doctor_id, billable, created_by, created_on, updated_by, updated_on, deleted,
                                          deleted_by, deleted_on, patient_medical_coverage_id)
SELECT (-offset_value-id), (-offset_value-patient_id), clinical_specialty_id, (-offset_value-institution_id), performed_date,
       (-offset_value-doctor_id), billable, (-offset_value-created_by), created_on,
       (-offset_value-updated_by), updated_on, deleted, (-offset_value-deleted_by), deleted_on,
       (-offset_value-patient_medical_coverage_id)
FROM  public.nursing_consultation;

--------------------------------------------------------------------------------------------------------------------
-- Recetas
INSERT INTO migrate.medication_request (id, patient_id, medical_coverage_id, status_id, intent_id, category_id,
                                        doctor_id, has_recipe, note_id, institution_id, request_date, created_by,
                                        updated_by, created_on, updated_on, deleted, deleted_by, deleted_on)
SELECT (-offset_value-id), (-offset_value-patient_id), (-offset_value-medical_coverage_id), status_id, intent_id,
       category_id, (-offset_value-doctor_id), has_recipe, (-offset_value-note_id), (-offset_value-institution_id),
       request_date, (-offset_value-created_by), (-offset_value-updated_by), created_on, updated_on, deleted,
       (-offset_value-deleted_by), deleted_on
FROM  public.medication_request;

--------------------------------------------------------------------------------------------------------------------
-- Ordenes
INSERT INTO migrate.service_request (id, patient_id, medical_coverage_id, status_id, intent_id, category_id, doctor_id,
                                     note_id, institution_id, request_date, created_by, updated_by, created_on,
                                     updated_on, deleted, deleted_by, deleted_on)
SELECT (-offset_value-id), (-offset_value-patient_id), (-offset_value-medical_coverage_id), status_id, intent_id,
       category_id, (-offset_value-doctor_id), (-offset_value-note_id), (-offset_value-institution_id),
       request_date, (-offset_value-created_by), (-offset_value-updated_by), created_on, updated_on, deleted,
       (-offset_value-deleted_by), deleted_on
FROM  public.service_request;
END;
$$;


CALL migrate(10000);