--liquibase formatted sql

-- Changeset bchacon:create-view-v_prescription_request_v2_2_36
-- runOnChange:false # NO TOCAR
-- splitStatements:true
-- endDelimiter:;
-- comment: Se pasa la definición de la query v2 utilizada para obtener las recetas, la misma provee soporte de dispensa de multiples comerciales
DROP VIEW IF EXISTS v_prescription_request_v2;
CREATE VIEW v_prescription_request_v2 AS (
    SELECT
        mr.id AS medication_request_id,
        ms.prescription_date,
        ms.due_date,
        p2.first_name AS patient_name,
        p2.last_name AS patient_last_name,
        pe.name_self_determination AS patient_self_perceived_name,
        g.description AS patient_gender,
        spg.description AS patient_self_perceived_gender,
        p2.birth_date AS patient_birth_date,
        it.description AS patient_identification_type,
        p2.identification_number AS patient_identification_number,
        mc.name AS mc_name,
        mc.cuit AS mc_cuit,
        mcp.plan AS mc_plan,
        pmc.affiliate_number AS mc_affliate_number,
        i.name AS i_name,
        i.sisa_code AS i_sisa_code,
        i.province_code AS i_province_code,
        CONCAT(a.street, ' ', a.number, ' ', CASE WHEN a.floor IS NOT NULL THEN CONCAT('Piso ', a.floor) ELSE '' END) AS institution_address,
        p3.first_name AS professional_name,
        p3.last_name AS professional_last_name,
        it2.description AS professional_identification_type,
        p3.identification_number AS professional_identification_number,
        pe2.phone_number AS professional_phone_number,
        pe2.email AS professional_email,
        ps.description AS professional_specialty,
        ps.sctid_code AS professional_specialty_sctid,
        pln.license_number AS professional_license_number,
        CASE WHEN pln.type_license_number = 1 THEN 'NACIONAL' ELSE 'PROVINCIAL' END AS professional_type_license_number,
        ms.prescription_line_number AS line,
        msls.description AS line_state,
        s.pt AS problem_snomed_pt,
        s.sctid AS problem_snomed_sctid,
        pt.description AS problem_type,
        s2.pt AS medication_snomed_pt,
        s2.sctid AS medication_snomed_sctid,
        d2.doses_by_unit AS dosis_by_unit,
        d2.doses_by_day AS dosis_by_day,
        d2.duration AS duration,
        '' AS presentation,
        mscp.medication_pack_quantity AS presentation_pack_quantity,
        d.id as document_id,
        mr.is_archived,
        CASE WHEN d2.dose_quantity_id IS NULL THEN NULL ELSE q.value END,
        msls.id AS status_id,
        ms.id AS medication_statement_id,
        co.description AS country,
        pr.description AS province,
        de.description AS department,
        ci.description AS city,
        pa.street AS person_street,
        pa.number AS person_street_number,
        n.description AS observation,
        d2.frequency AS frequency,
        d2.period_unit AS frequency_unit,
        cs.name AS specialty,
        cs.sctid_code AS specialty_snomed_id,
        s3.sctid AS suggested_commercial_medication_sctid,
        s3.pt AS suggested_commercial_medication_pt,
        mscp.presentation_unit_quantity,
        q.unit AS quantity_unit
    FROM medication_statement ms
        JOIN document_medicamention_statement dms ON (ms.id = dms.medication_statement_id)
        JOIN document d ON (d.id = dms.document_id)
        JOIN medication_request mr ON (mr.id = d.source_id)
        LEFT JOIN clinical_specialty cs ON (mr.clinical_specialty_id = cs.id)
        JOIN patient p ON (p.id = ms.patient_id)
        JOIN person p2 ON (p2.id = p.person_id)
        LEFT JOIN person_extended pe ON (pe.person_id = p2.id)
        JOIN gender g ON (g.id = p2.gender_id)
        LEFT JOIN self_perceived_gender spg ON (spg.id = pe.gender_self_determination)
        JOIN identification_type it ON (it.id = p2.identification_type_id)
        LEFT JOIN patient_medical_coverage pmc ON (mr.medical_coverage_id = pmc.id)
        LEFT JOIN medical_coverage mc ON (mc.id = pmc.medical_coverage_id)
        LEFT JOIN medical_coverage_plan mcp ON (mcp.medical_coverage_id = pmc.medical_coverage_id)
        LEFT JOIN institution i ON (i.id = d.institution_id)
        LEFT JOIN address a ON (a.id = i.address_id)
        LEFT JOIN address pa ON (pa.id = pe.address_id)
        LEFT JOIN country co ON (pa.country_id = co.id)
        LEFT JOIN province pr ON (pa.province_id = pr.id)
        LEFT JOIN department de ON (pa.department_id = de.id)
        LEFT JOIN city ci ON (pa.city_id = ci.id)
        JOIN healthcare_professional hp ON (hp.id = mr.doctor_id)
        JOIN person p3 ON (p3.id = hp.person_id)
        JOIN identification_type it2 ON (it2.id = p3.identification_type_id)
        LEFT JOIN person_extended pe2 ON (pe2.person_id = p3.id)
        JOIN professional_professions pp ON (pp.healthcare_professional_id = hp.id)
        JOIN healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id)
        JOIN professional_specialty ps ON (ps.id = pp.professional_specialty_id)
        JOIN professional_license_numbers pln ON (pln.professional_profession_id = pp.id OR pln.healthcare_professional_specialty_id = hps.id)
        JOIN health_condition hc ON (hc.id = ms.health_condition_id)
        JOIN snomed s ON (s.id = hc.snomed_id)
        JOIN problem_type pt ON (pt.id = hc.problem_id)
        JOIN snomed s2 ON (ms.snomed_id = s2.id)
        LEFT JOIN dosage d2 ON (d2.id = ms.dosage_id)
        LEFT JOIN quantity q ON (d2.dose_quantity_id = q.id)
        LEFT JOIN medication_statement_line_state msls ON (msls.id = ms.prescription_line_state)
        LEFT JOIN note n ON (ms.note_id = n.id)
        LEFT JOIN snomed s3 ON (s3.id = ms.suggested_commercial_medication_snomed_id)
        LEFT JOIN medication_statement_commercial_prescription mscp ON (mscp.medication_statement_id = ms.id)
    WHERE (d.type_id = 5 OR d.type_id = 14)
    AND hc.verification_status_id = '59156000'
    AND (ms.status_id = '255594003' OR ms.status_id = '55561003')
    ORDER BY mr.id DESC
);