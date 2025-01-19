--liquibase formatted sql

-- Changeset efernandez:create-view-v_reasons_2_31_0
-- runOnChange:false # NO TOCAR
-- splitStatements:true
-- endDelimiter:;
-- comment: Se pasa la definición de v_reasons a un archivo aparte.
-- Hay un nuevo tipo de documento para las notas de evolución de enfermería de guardia: NURSING_EMERGENCY_CARE_EVOLUTION 21.
-- Se agrega el nuevo id (21) a todos los WHEREs donde se busquen notas de evolución de guardia.
DROP VIEW IF EXISTS v_reasons;
CREATE VIEW v_reasons AS (
    SELECT
        r.id, r.description, d.id as document_id , d.source_type_id, d.type_id as document_type_id, d.source_id
    FROM
        emergency_care_episode_reason ecer
        JOIN reasons r on (ecer.reason_id = r.id)
        JOIN document d on (d.source_id = ecer.emergency_care_episode_id AND d.source_type_id = 4)
UNION
    SELECT
        r.id, r.description, d.id as document_id , d.source_type_id, d.type_id as document_type_id, d.source_id
    FROM
        odontology_consultation_reason  ocr1
        JOIN reasons r on (ocr1.reason_id = r.id)
        JOIN document d on (d.source_id = ocr1.odontology_consultation_id AND d.source_type_id = 6)
UNION
    SELECT
        r.id, r.description, d.id as document_id , d.source_type_id, d.type_id as document_type_id, d.source_id
    FROM
        outpatient_consultation_reasons ocr
        JOIN reasons r on (ocr.reason_id = r.id)
        JOIN document d on (d.source_id = ocr.outpatient_consultation_id AND d.source_type_id = 1)
UNION
    SELECT
        r.id, r.description, d.id as document_id , d.source_type_id, d.type_id as document_type_id, d.source_id
    FROM
        emergency_care_evolution_note_reason ecenr
        JOIN emergency_care_evolution_note ecen on (ecenr.emergency_care_evolution_note_id = ecen.id)
        JOIN reasons r on (ecenr.reason_id = r.id)
        JOIN "document" d on (d.id = ecen.document_id and d.type_id IN (16, 21))
);