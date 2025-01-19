--liquibase formatted sql

-- Changeset lmanterola:modify-view-v_attention_2_36
-- runOnChange:false # NO TOCAR
-- splitStatements:true
-- endDelimiter:;
-- comment: se agregan las actividades de atenciones de enfermería
DROP VIEW IF EXISTS v_attention;
CREATE VIEW v_attention AS
(
SELECT d.id                     as id,
       oc.patient_id            as patient_id,
       oc.institution_id        as institution_id,
       oc.clinical_specialty_id as clinical_speciality_id,
       oc.start_date            as performed_date,
       d.source_id              as encounter_id,
       d.source_type_id         as scope_id,
       oc.doctor_id             as doctor_id,
       oc.created_by            as created_by,
       oc.created_on            as created_on,
       oc.updated_by            as updated_by,
       oc.updated_on            as updated_on,
       oc.deleted_by            as deleted_by,
       oc.deleted_on            as deleted_on,
       oc.deleted               as deleted
FROM outpatient_consultation as oc
         JOIN document as d ON (oc.id = d.source_id and d.source_type_id = 1)
UNION ALL
SELECT d.id as id,
       ie.patient_id,
       ie.institution_id,
       ie.clinical_specialty_id,
       ie.entry_date,
       d.source_id,
       d.source_type_id,
       hpg.healthcare_professional_id,
       d.created_by,
       d.created_on,
       ie.updated_by,
       d.updated_on,
       d.deleted_by,
       d.deleted_on,
       d.deleted
FROM internment_episode as ie
         JOIN document AS d ON (ie.id = d.source_id and d.source_type_id = 0)
         JOIN healthcare_professional_group AS hpg ON (ie.id = hpg.internment_episode_id and hpg.responsible)
UNION ALL
SELECT d.id as id,
       dc.patient_id,
       dc.institution_id,
       dc.clinical_specialty_id,
       dc.performed_date,
       d.source_id,
       d.source_type_id,
       dc.doctor_id,
       dc.created_by,
       dc.created_on,
       dc.updated_by,
       dc.updated_on,
       dc.deleted_by,
       dc.deleted_on,
       dc.deleted
FROM odontology_consultation as dc
         JOIN document as d ON (dc.id = d.source_id and d.source_type_id = 6)
UNION ALL
SELECT d.id as id,
       vc.patient_id,
       vc.institution_id,
       vc.clinical_specialty_id,
       vc.performed_date,
       d.source_id,
       d.source_type_id,
       vc.doctor_id,
       vc.created_by,
       vc.created_on,
       vc.updated_by,
       vc.updated_on,
       vc.deleted_by,
       vc.deleted_on,
       vc.deleted
FROM vaccine_consultation as vc
         JOIN document as d ON (vc.id = d.source_id and d.source_type_id = 5)
UNION ALL
SELECT d.id AS id,
       ecen.patient_id,
       ecen.institution_id,
       ecen.clinical_specialty_id,
       ecen.start_date,
       d.source_id,
       d.source_type_id,
       ecen.doctor_id,
       ecen.created_by,
       ecen.created_on,
       ecen.updated_by,
       ecen.updated_on,
       ecen.deleted_by,
       ecen.deleted_on,
       ecen.deleted
FROM emergency_care_evolution_note ecen
         JOIN document d ON (d.id = ecen.document_id AND d.source_type_id = 4)

UNION ALL

SELECT d.id AS id,
       nc.patient_id,
       nc.institution_id,
       nc.clinical_specialty_id,
       nc.performed_date,
       d.source_id,
       d.source_type_id,
       nc.doctor_id,
       nc.created_by,
       nc.created_on,
       nc.updated_by,
       nc.updated_on,
       nc.deleted_by,
       nc.deleted_on,
       nc.deleted
FROM nursing_consultation nc
    JOIN document d ON (d.source_id = nc.id AND d.source_type_id = 7)
)