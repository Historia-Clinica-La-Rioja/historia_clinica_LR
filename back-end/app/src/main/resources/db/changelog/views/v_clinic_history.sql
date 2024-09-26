--liquibase formatted sql

-- Changeset mromero:create-view-clinic_history_2_31_0_2
-- runOnChange:false # NO TOCAR
-- splitStatements:true
-- endDelimiter:;
-- comment: se agregan fecha y hora en algunas secciones del parte anestésico. (Se crea un nuevo changeset para su testing)
--
DROP VIEW IF EXISTS v_clinic_history;
CREATE VIEW v_clinic_history AS (
SELECT d.id AS id,
      d.source_id AS source_id,
      d.patient_id AS patient_id,
      d.source_type_id AS source_type_id,
      d.created_on,
      d.created_by,
      d.type_id AS document_type_id,
      dt.description AS document_type,
      cs.name AS clinical_specialty,
      i.id AS institution_id,
      i.name AS institution,
      d.patient_age_period AS patient_age_period,
      (SELECT sr.source_type_id FROM service_request sr WHERE d.type_id = 6 AND d.source_id = sr.id) AS request_source_type_id,
      (SELECT sr.source_id FROM service_request sr WHERE d.type_id = 6 AND d.source_id = sr.id) AS request_source_id,
      (SELECT MIN(ie.entry_date)
       FROM internment_episode ie
                LEFT JOIN service_request sr ON (sr.source_id = ie.id)
       WHERE (d.source_id = ie.id AND d.source_type_id = 0 AND d.type_id <> 6)
          OR (d.type_id = 6 AND sr.source_type_id = 0 AND d.source_id = sr.id AND sr.source_id = ie.id)) AS internment_start_date,
      (SELECT MAX(pd.administrative_discharge_date)
       FROM internment_episode ie
                LEFT JOIN service_request sr ON (sr.source_id = ie.id)
                LEFT JOIN patient_discharge pd ON (pd.internment_episode_id = ie.id)
       WHERE (d.source_id = ie.id AND d.source_type_id = 0 AND d.type_id <> 6)
          OR (d.type_id = 6 AND sr.source_type_id = 0 AND d.source_id = sr.id AND sr.source_id = ie.id)) AS internment_end_date,
      (SELECT MIN(ece.created_on)
       FROM emergency_care_episode ece
                LEFT JOIN service_request sr ON (sr.source_id = ece.id)
       WHERE (d.source_id = ece.id AND d.source_type_id = 4 AND d.type_id <> 6)
          OR (d.type_id = 6 AND sr.source_type_id = 4 AND d.source_id = sr.id AND sr.source_id = ece.id)) AS emergency_care_start_date,
      (SELECT MAX(ecd.administrative_discharge_on)
       FROM emergency_care_episode ece
                LEFT JOIN service_request sr ON (sr.source_id = ece.id)
                JOIN emergency_care_discharge ecd ON (ecd.emergency_care_episode_id = ece.id)
       WHERE (d.source_id = ece.id AND d.source_type_id = 4 AND d.type_id <> 6)
          OR (d.type_id = 6 AND sr.source_type_id = 4 AND d.source_id = sr.id AND sr.source_id = ece.id)) AS emergency_care_end_date,
      (SELECT (CASE WHEN(
          (SELECT count(*) filter (WHERE dr.status_id = '1')
           FROM document_diagnostic_report ddr
                    JOIN diagnostic_report dr ON (dr.id = ddr.diagnostic_report_id)
           WHERE ddr.document_id = d.id AND d.type_id= 6 GROUP BY ddr.document_id)
              =
          (SELECT count(*) filter (WHERE dr.status_id = '261782000' OR dr.status_id = '89925002')
           FROM document_diagnostic_report ddr
                    JOIN diagnostic_report dr
                         ON (dr.id = ddr.diagnostic_report_id)
           WHERE ddr.document_id = d.id AND d.type_id = 6 GROUP BY ddr.document_id))
                        THEN
                        (SELECT MAX(dr.created_on)
                         FROM document_diagnostic_report ddr
                                  JOIN diagnostic_report dr ON (dr.id = ddr.diagnostic_report_id) WHERE ddr.document_id = d.id AND dr.status_id = '261782000')
          END)) AS service_request_end_date,
      (SELECT MAX(ms.updated_on)
       FROM document_medicamention_statement dms
                JOIN medication_statement ms ON (ms.id = dms.medication_statement_id)
       WHERE dms.document_id = d.id AND d.type_id = 5 AND ms.status_id IN ('255594003', '6155003', '385655000')) AS medication_end_date,
      coalesce('Diagnósticos: ' || (SELECT string_agg((CASE WHEN hc.main THEN 'Principal: ' || s.pt ELSE 'Otro: ' || s.pt END) ||
                                                      (CASE WHEN hc.status_id IN ('723506003', '277022003', '73425007') THEN '|(Eliminado el día ' || to_char(hc.start_date ,'dd/MM/yyyy') || ')' ELSE '' END), '| ')
                                    FROM document_health_condition dhc
                                             JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
                                             JOIN snomed s ON (s.id = hc.snomed_id)
                                    WHERE dhc.document_id = d.id AND d.type_id IN (1, 2, 3, 13, 16, 20) AND hc.problem_id = '439401001' GROUP BY dhc.document_id), '') ||
      coalesce(CASE WHEN d.type_id = 10 THEN 'Problema asociado: ' ELSE 'Problemas: ' END ||
               (SELECT string_agg(s.pt || (CASE WHEN hc.status_id = '-55607006' THEN '|(Crónico)' ELSE '' END) ||
                                  (CASE WHEN hc.start_date IS NOT NULL AND d.type_id <> 10 THEN '|(desde ' || to_char(hc.start_date ,'dd/MM/yyyy') ELSE '' END) ||
                                  (CASE WHEN hc.inactivation_date IS NOT NULL AND d.type_id <> 10 THEN ' hasta ' || to_char(hc.inactivation_date, 'dd/MM/yyyy') ELSE '' END) ||
                                  (CASE WHEN perl.description IS NOT NULL AND d.type_id <> 10 THEN ', motivo: ' || perl.description ELSE '' END) ||
                                  (CASE WHEN n.description IS NOT NULL AND d.type_id <> 10 THEN ', observaciones: ' || n.description ELSE '' END) ||
                                  (CASE WHEN hc.start_date IS NOT NULL AND d.type_id <> 10 THEN ')' ELSE '' END), '| ')
                FROM document_health_condition dhc
                         JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
                         LEFT JOIN note n ON (hc.note_id = n.id)
                         LEFT JOIN problem_error_reason per ON (hc.id = per.health_condition_id)
                         LEFT JOIN problem_error_reasons_list perl ON (per.reason_id = perl.id)
                         JOIN snomed s ON (s.id = hc.snomed_id)
                WHERE dhc.document_id = d.id AND d.type_id IN (4, 7, 10) AND hc.problem_id IN ('55607006', '-55607006') GROUP BY dhc.document_id), '') ||
      coalesce('Problemas asociados: ' || (SELECT string_agg(r.description, '| ')
                                           FROM emergency_care_episode_reason ecer
                                                    JOIN reasons r ON (ecer.reason_id=r.id)
                                           WHERE d.type_id = 15 AND d.source_id = ecer.emergency_care_episode_id), '') ||
      coalesce('Motivos de consulta: ' || (SELECT string_agg(or1.description , '| ')
                                           FROM odontology_consultation_reason ocr
                                                    JOIN odontology_reason or1 ON (or1.id = ocr.reason_id)
                                           WHERE ocr.odontology_consultation_id = d.source_id AND d.type_id = 9 ), '') ||
      coalesce('Problemas: ' || (SELECT string_agg(DISTINCT s.pt, '| ')
                                 FROM document_medicamention_statement dms
                                          JOIN medication_statement ms ON (ms.id = dms.medication_statement_id)
                                          LEFT OUTER JOIN health_condition hc ON (hc.id = ms.health_condition_id)
                                          LEFT OUTER JOIN snomed s ON (s.id = hc.snomed_id)
                                 WHERE dms.document_id = d.id AND d.type_id IN (5, 14) GROUP BY dms.document_id), '') ||
      coalesce('Problemas: ' || (SELECT string_agg(s.pt, '| ' )
                                 FROM document_indication di
                                          JOIN indication i ON (i.id = di.indication_id )
                                          JOIN pharmaco ph ON (ph.id = i.id)
                                          JOIN health_condition hc ON (hc.id = ph.health_condition_id)
                                          JOIN snomed s ON (s.id = hc.snomed_id)
                                 WHERE d.id = di.document_id AND d.type_id = 12 GROUP BY di.document_id), '') ||
      coalesce('Diagnóstico asociado: ' || (SELECT string_agg(DISTINCT s.pt, '| ')
                                            FROM document_diagnostic_report ddr
                                                     JOIN diagnostic_report dr ON (dr.id = ddr.diagnostic_report_id)
                                                     JOIN health_condition hc ON (hc.id = dr.health_condition_id)
                                                     JOIN snomed s ON (s.id = hc.snomed_id)
                                            WHERE ddr.document_id = d.id AND d.type_id = 6 GROUP BY ddr.document_id), '') ||
      coalesce('Problemas asociados: ' || (SELECT string_agg(DISTINCT s.pt, '| ')
                                           FROM counter_reference cr
                                                    JOIN reference_health_condition rhc ON (cr.reference_id = rhc.reference_id)
                                                    JOIN health_condition hc ON (hc.id = rhc.health_condition_id)
                                                    JOIN snomed s ON (s.id = hc.snomed_id)
                                           WHERE d.source_id = cr.id AND d.type_id = 11), '') AS problems,
      coalesce('Antecedentes familiares: ' || coalesce ((SELECT string_agg(s.pt || (CASE WHEN hc.start_date IS NOT NULL THEN ' (desde ' || to_char(hc.start_date, 'dd/MM/yyyy') || ')' ELSE '' END), ', ')
                                                         FROM document_health_condition dhc
                                                                  JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
                                                                  JOIN snomed s ON (s.id = hc.snomed_id)
                                                         WHERE dhc.document_id = d.id
                                                           AND d.type_id IN (1, 3, 4, 16)
                                                           AND hc.problem_id = '57177007'
                                                         GROUP BY dhc.document_id),
                                                        (SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
                                                         FROM document_referable_concept drc
                                                         WHERE drc.document_id = d.id
                                                           AND drc.referable_concept_id = 3)), '') AS family_record,
      coalesce(CASE WHEN d.type_id = 9 THEN 'Otros diagnósticos y antecedentes personales: ' ELSE 'Antecedentes personales: ' END ||
               coalesce((SELECT string_agg(s.pt ||
                                           (CASE WHEN hc.start_date IS NOT NULL THEN ' (desde ' || to_char(hc.start_date ,'dd/MM/yyyy') ELSE '' END) ||
                                           (CASE WHEN hc.inactivation_date IS NOT NULL THEN ' hasta ' || to_char(hc.inactivation_date, 'dd/MM/yyyy') ELSE '' END) ||
                                           (CASE WHEN pht.description IS NOT NULL THEN ', tipo: ' || pht.description ELSE '' END) ||
                                           (CASE WHEN n.description IS NOT NULL THEN ', observaciones: ' || n.description ELSE '' END) ||
                                           (CASE WHEN hc.start_date IS NOT NULL THEN ')' ELSE '' END), '| ')
                         FROM document_health_condition dhc
                                  JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
                                  LEFT JOIN note n ON (hc.note_id = n.id)
                                  LEFT JOIN personal_history ph ON (hc.id = ph.health_condition_id)
                                  LEFT JOIN personal_history_type pht ON (ph.type_id = pht.id)
                                  JOIN snomed s ON (s.id = hc.snomed_id)
                         WHERE dhc.document_id = d.id
                           AND hc.problem_id IN ('00000002')
                         GROUP BY dhc.document_id),
                        (
                            SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
                            FROM document_referable_concept drc
                            WHERE drc.document_id = d.id
                              AND drc.referable_concept_id = 2
                        )), '') AS personal_record,
      coalesce('Procedimientos: ' || (SELECT string_agg(CASE WHEN p.performed_date IS NOT NULL THEN s.pt || ' (' || to_char(p.performed_date, 'dd/MM/yyyy') || ')' ELSE s.pt END ,', ')
                                      FROM document_procedure dp
                                               LEFT OUTER JOIN "procedures" p  ON (p.id=dp.procedure_id)
                                               LEFT OUTER JOIN snomed s ON (s.id=p.snomed_id)
                                      WHERE dp.document_id = d.id AND d.type_id IN (1, 2, 3, 4, 9, 10, 11, 16) GROUP BY dp.document_id),'') AS procedures,
      coalesce((SELECT string_agg(s.pt ||
                                  (CASE WHEN dos.period_unit = 'd' THEN '\nUna aplicación por día' ELSE '' END) ||
                                  (CASE WHEN dos.period_unit = 'h' THEN '\nUna aplicación cada ' || dos.frequency || 'hs.' ELSE '' END) ||
                                  (CASE WHEN dos.chronic = TRUE THEN ', uso crónico' ELSE '' END) ||
                                  (CASE WHEN dos.duration IS NOT NULL THEN ' durante ' || dos.duration || ' días' ELSE '' END) ||
                                  (CASE WHEN ms.note_id IS NOT NULL THEN '\nObservaciones: ' || n.description ELSE '' END) ||
                                  (CASE WHEN hc.snomed_id IS NOT NULL THEN '\nProblema asociado: ' || s2.pt ELSE '' END) ||
                                  (CASE WHEN ms.status_id = '6155003' THEN '\nFinalizada el día ' || to_char(ms.updated_on, 'dd/MM/yyyy') ELSE '' END) ||
                                  (CASE WHEN ms.status_id = '385655000' THEN '\nSuspendida el día ' || to_char(ms.updated_on, 'dd/MM/yyyy') ELSE '' END) , '\n\n')
                FROM document_medicamention_statement dms
                         JOIN medication_statement ms  ON (ms.id=dms.medication_statement_id)
                         JOIN medication_statement_status mss ON (ms.status_id=mss.id)
                         JOIN snomed s ON (s.id=ms.snomed_id)
                         LEFT OUTER JOIN note n ON (n.id = ms.note_id)
                         LEFT OUTER JOIN health_condition hc ON (hc.id=ms.health_condition_id)
                         LEFT OUTER JOIN snomed s2 ON (s2.id = hc.snomed_id)
                         LEFT OUTER JOIN dosage dos ON (ms.dosage_id=dos.id)
                WHERE dms.document_id = d.id AND d.type_id IN (5, 14) GROUP BY dms.document_id),'') ||
      coalesce('Medicación habitual: '|| (SELECT string_agg(s.pt ||
                                                            (CASE WHEN ms.note_id IS NOT NULL THEN ' (' || n.description || ')' ELSE '' END) ||
                                                            (CASE WHEN ms.status_id = '6155003' THEN ' (finalizada el día ' || to_char(ms.updated_on, 'dd/MM/yyyy') || ')' ELSE '' END) ||
                                                            (CASE WHEN ms.status_id = '385655000' THEN ' (suspendida el día ' || to_char(ms.updated_on, 'dd/MM/yyyy') || ')' ELSE '' END), '. ')
                                          FROM document_medicamention_statement dms
                                                   JOIN medication_statement ms  ON (ms.id=dms.medication_statement_id)
                                                   JOIN medication_statement_status mss ON (ms.status_id=mss.id)
                                                   JOIN snomed s ON (s.id=ms.snomed_id)
                                                   LEFT OUTER JOIN note n ON (n.id = ms.note_id)
                                          WHERE dms.document_id = d.id AND d.type_id IN (1, 3, 4, 9, 11, 16, 20) GROUP BY dms.document_id),'') AS medicines,
      coalesce('Alergias: '|| coalesce ((
                                            SELECT string_agg(s.pt,', ')
                                            FROM document_allergy_intolerance dai
                                                     JOIN allergy_intolerance ai ON (ai.id=dai.allergy_intolerance_id)
                                                     JOIN snomed s ON (s.id=ai.snomed_id)
                                            WHERE dai.document_id = d.id
                                              AND d.type_id IN (1, 2, 3, 4, 9, 11, 16)
                                            GROUP BY dai.document_id),
                                        (SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
                                         FROM document_referable_concept drc
                                         WHERE drc.document_id = d.id
                                           AND drc.referable_concept_id = 1)),
               '') AS allergies,
      coalesce('Vacunas: ' || (SELECT string_agg(CASE WHEN i.administration_date IS NOT NULL THEN s.pt || ' (' || to_char(i.administration_date, 'dd/MM/yyyy') || ')' ELSE s.pt END,', ')
                               FROM document_inmunization di
                                        JOIN inmunization i ON (i.id = di.inmunization_id)
                                        LEFT OUTER JOIN snomed s ON (s.id = i.snomed_id)
                               WHERE di.document_id = d.id AND d.type_id IN (1, 2, 3, 4, 8, 12, 13) GROUP BY di.document_id ), '') AS vaccines,
      coalesce('Grupo y factor sanguíneo: '|| (SELECT string_agg(replace(replace(s.pt, 'hallazgo relacionado con el grupo sanguíneo', ''),'(hallazgo)','') || ol.value, ', ')
                                               FROM document_lab dl
                                                        JOIN observation_lab ol ON (ol.id=dl.observation_lab_id)
                                                        JOIN snomed s ON (s.id=ol.snomed_id)
                                               WHERE dl.document_id = d.id AND d.type_id IN (1, 2, 4, 10, 13, 16, 20) AND ol.status_id = '261782000' GROUP BY dl.document_id),'') AS blood_type,
      coalesce('Datos antropométricos: '|| (SELECT string_agg(replace(s.pt, '(entidad observable)', '') || ': ' || ovs.value || coalesce(CASE WHEN s.sctid IN ('50373000', '363812007') THEN 'cm' ELSE 'kg' END,''), ', ')
                                            FROM document_vital_sign dvs
                                                     JOIN observation_vital_sign ovs ON (ovs.id=dvs.observation_vital_sign_id)
                                                     JOIN snomed s ON (s.id=ovs.snomed_id)
                                            WHERE dvs.document_id = d.id AND s.sctid IN ('50373000', '27113001', '363812007') AND ovs.status_id = '261782000' GROUP BY dvs.document_id),'') AS anthropometric_data,
      coalesce('Signos vitales y factores de riesgo: '|| (SELECT string_agg(replace(replace(s.pt, '(entidad observable)', ''), '(sustancia)', '') || ': ' || ovs.value ||
                                                                            coalesce(CASE WHEN s.sctid IN ('364075005', '86290005') THEN '/min' ELSE
                                                                                CASE WHEN s.sctid IN ('703421000') THEN '°C' ELSE
                                                                                    CASE WHEN s.sctid IN ('103228002', '259689004', '827181004', '28317006') THEN '%' ELSE
                                                                                        CASE WHEN s.sctid IN ('271649006', '271650006') THEN 'mmHg' ELSE
                                                                                            CASE WHEN s.sctid IN ('434912009') THEN 'mg/dl' ELSE ''
                                                                                                END END END END END, ''), ', ')
                                                          FROM document_vital_sign dvs
                                                                   JOIN observation_vital_sign ovs ON (ovs.id=dvs.observation_vital_sign_id)
                                                                   JOIN snomed s ON (s.id=ovs.snomed_id)
                                                          WHERE dvs.document_id = d.id AND d.type_id IN (1, 2, 4, 10, 13, 16, 20) AND s.sctid NOT IN ('50373000', '27113001', '363812007') AND ovs.status_id = '261782000' GROUP BY dvs.document_id),'') ||
      coalesce('Signos vitales y factores de riesgo: '|| (SELECT string_agg(replace(replace(s.pt, '(entidad observable)', ''), '(sustancia)', '') || ': ' || ovs.value ||
                                                                            coalesce(CASE WHEN s.sctid IN ('364075005', '86290005') THEN '/min' ELSE
                                                                                CASE WHEN s.sctid IN ('703421000') THEN '°C' ELSE
                                                                                    CASE WHEN s.sctid IN ('103228002', '259689004', '827181004', '28317006') THEN '%' ELSE
                                                                                        CASE WHEN s.sctid IN ('271649006', '271650006') THEN 'mmHg' ELSE
                                                                                            CASE WHEN s.sctid IN ('434912009') THEN 'mg/dl' ELSE ''
                                                                                                END END END END END, ''), ', ')
                                                          FROM triage_vital_signs tvs
                                                                   JOIN observation_vital_sign ovs ON (ovs.id = tvs.observation_vital_sign_id)
                                                                   JOIN snomed s ON (s.id = ovs.snomed_id)
                                                                   JOIN triage t ON (t.id = tvs.triage_id)
                                                                   JOIN document_triage dt ON (t.id = dt.triage_id)
                                                          WHERE dt.document_id = d.id AND d.type_id = 15 AND ovs.status_id = '261782000'), '') AS risk_factors,
      coalesce('Otras circunstancias que prolongan la epicrisis: ' || (SELECT string_agg(s.pt, ', ')
                                                                       FROM document_health_condition dhc
                                                                                JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
                                                                                JOIN snomed s ON (s.id = hc.snomed_id)
                                                                       WHERE dhc.document_id = d.id AND d.type_id = 3 AND hc.problem_id = '00000001' GROUP BY dhc.document_id), '') AS epicrisis_other_circumstances,
      coalesce('Causa externa de traumatismo, envenenamiento y otros efectos adversos: ' || (SELECT string_agg(
                                                                                                            (CASE WHEN ec.external_cause_type_id IS NOT NULL THEN '\nProducido por: ' || ect.description || '. ' ELSE '' END) ||
                                                                                                            (CASE WHEN ec.event_location IS NOT NULL THEN '\nLugar donde ocurrió: ' || (
                                                                                                                (CASE WHEN ec.event_location = 1 THEN 'Domicilio particular. ' ELSE '' END) ||
                                                                                                                (CASE WHEN ec.event_location = 2 THEN 'Vía pública. ' ELSE '' END) ||
                                                                                                                (CASE WHEN ec.event_location = 3 THEN 'Lugar de trabajo. ' ELSE '' END) ||
                                                                                                                (CASE WHEN ec.event_location = 4 THEN 'Otro. ' ELSE '' END)) ELSE '' END ) ||
                                                                                                            (CASE WHEN ec.snomed_id IS NOT NULL THEN '\nCómo se produjo: ' || s.pt ELSE '' END), ', ')
                                                                                             FROM document_external_cause dec2
                                                                                                      JOIN external_cause ec ON (ec.id = dec2.external_cause_id)
                                                                                                      LEFT OUTER JOIN external_cause_type ect ON (ect.id = ec.external_cause_type_id)
                                                                                                      LEFT OUTER JOIN snomed s ON (s.id = ec.snomed_id)
                                                                                             WHERE dec2.document_id = d.id AND d.type_id = 3 AND (ec.snomed_id IS NOT NULL OR ec.external_cause_type_id IS NOT NULL OR ec.event_location IS NOT NULL)), '') AS epicrisis_external_cause,
      coalesce('Datos del evento obstétrico: ' || (SELECT string_agg(
                                                                  (CASE WHEN oe.previous_pregnancies IS NOT NULL THEN '\nGestas anteriores: ' || oe.previous_pregnancies ELSE '' END) ||
                                                                  (CASE WHEN oe.current_pregnancy_end_date IS NOT NULL THEN '\nFecha de término de gesta actual: ' || to_char(oe.current_pregnancy_end_date, 'dd/MM/yyyy') ELSE '' END) ||
                                                                  (CASE WHEN oe.gestational_age IS NOT NULL THEN '\nEdad gestacional de gesta actual: ' || oe.gestational_age ELSE '' END) ||
                                                                  (CASE WHEN oe.pregnancy_termination_type IS NOT NULL THEN '\nTerminación de gesta actual: ' ||
                                                                                                                            (CASE WHEN oe.pregnancy_termination_type = 1 THEN 'Vaginal' ELSE '' END) ||
                                                                                                                            (CASE WHEN oe.pregnancy_termination_type = 2 THEN 'Cesárea' ELSE '' END) ||
                                                                                                                            (CASE WHEN oe.pregnancy_termination_type = 3 THEN 'Sin definir' ELSE '' END) ELSE '' END), ', ')
                                                   FROM document_obstetric_event doe
                                                            JOIN obstetric_event oe ON (oe.id = doe.obstetric_event_id)
                                                   WHERE doe.document_id = d.id AND d.type_id = 3
                                                     AND (oe.previous_pregnancies IS NOT NULL OR oe.current_pregnancy_end_date IS NOT NULL OR oe.gestational_age IS NOT NULL OR oe.pregnancy_termination_type IS NOT NULL) ), '') AS epicrisis_obstetric_event,
      coalesce('Pediatría: ' || (SELECT string_agg(
                                                (CASE WHEN bt.description IS NOT NULL THEN 'temperatura: ' || bt.description || ', ' ELSE '' END) ||
                                                (CASE WHEN td.crying_excessive IS TRUE THEN 'llanto sin consuelo, ' ELSE '' END) ||
                                                (CASE WHEN mh.description IS NOT NULL THEN 'tono muscular: ' || mh.description || ', ' ELSE '' END) ||
                                                (CASE WHEN rr.description IS NOT NULL THEN 'respiración con tiraje: ' || rr.description || ', ' ELSE '' END) ||
                                                (CASE WHEN td.stridor IS TRUE THEN 'respiración con estridor, ' ELSE '' END) ||
                                                (CASE WHEN p.description IS NOT NULL THEN 'perfusión: ' || p.description ELSE '' END), ', ')
                                 FROM triage_details td
                                          LEFT OUTER JOIN body_temperature bt ON (td.body_temperature_id  = bt.id)
                                          LEFT OUTER JOIN muscle_hypertonia mh ON (mh.id = td.muscle_hypertonia_id)
                                          LEFT OUTER JOIN respiratory_retraction rr ON (rr.id = td.respiratory_retraction_id)
                                          LEFT OUTER JOIN perfusion p ON (p.id = td.perfusion_id)
                                          JOIN triage t ON (t.id = td.triage_id)
                                          JOIN document_triage dt ON (t.id = dt.triage_id)
                                 WHERE dt.document_id = d.id AND d.type_id = 15), '') AS pediatric_risk_factors,
      coalesce('\nSolicitudes de referencia:\n' ||
               (WITH temporal_references AS
                         (SELECT DISTINCT re.id AS reference_id, s.id AS snomed_id
                          FROM reference re
                                   JOIN reference_health_condition rhc ON (re.id = rhc.reference_id )
                                   JOIN health_condition hc ON (hc.id = rhc.health_condition_id)
                                   JOIN snomed s ON (s.id = hc.snomed_id)
                          WHERE re.encounter_id = d.source_id AND d.type_id IN (4, 9) AND d.source_type_id = re.source_type_id AND re.deleted IS NULL OR re.deleted = FALSE)
                SELECT string_agg('\nProblemas asociados: ' ||
                                  (SELECT string_agg(s2.pt, ',')
                                   FROM reference re2
                                            JOIN reference_health_condition rhc2 ON (re2.id = rhc2.reference_id)
                                            JOIN health_condition hc2 ON (hc2.id = rhc2.health_condition_id)
                                            JOIN snomed s2 ON (s2.id = hc2.snomed_id)
                                   WHERE re2.id = re.id AND s2.id IN (SELECT snomed_id FROM temporal_references WHERE re2.id = reference_id)) ||
                                  (CASE WHEN re.care_line_id IS NOT NULL THEN '\nLinea de cuidado: ' || cl.description ELSE '' END ) || '\nEspecialidad: ' || clinical_specialties || '\nInstitución destino: ' || i.name ||
                                  (CASE WHEN re.reference_note_id IS NOT NULL THEN '\nObservaciones: ' || rn.description ELSE '' END),'\n')
                FROM reference re
                         JOIN (
                    SELECT re2.id, STRING_AGG(cs."name" , ', ') AS clinical_specialties
                    FROM reference re2
                             JOIN reference_clinical_specialty rcs ON (rcs.reference_id = re2.id)
                             JOIN clinical_specialty cs ON (cs.id = rcs.clinical_specialty_id)
                    GROUP BY re2.id
                ) AS reference_clinical_specialties ON (reference_clinical_specialties.id = re.id)
                         JOIN institution i ON (i.id = re.destination_institution_id)
                         LEFT OUTER JOIN care_line cl ON (cl.id = re.care_line_id)
                         LEFT OUTER JOIN reference_note rn ON (rn.id = re.reference_note_id)
                WHERE re.encounter_id = d.source_id AND d.type_id IN (4, 9) AND re.source_type_id = d.source_type_id GROUP BY d.id), '') AS outpatient_references,
      coalesce('Categoría: '|| (SELECT string_agg(src.description,'' )
                                FROM service_request sr
                                         LEFT OUTER JOIN service_request_category src  ON (sr.category_id=src.id)
                                         LEFT OUTER JOIN service_request_status srs ON (sr.status_id=srs.id)
                                WHERE sr.id = d.source_id AND d.type_id = 6),'') AS service_request_category,
      coalesce(
              (WITH temporal_orders AS
                        (SELECT DISTINCT d.source_id AS source_id, dr.snomed_id AS snomed_id, dr.health_condition_id AS health_condition_id
                         FROM document_diagnostic_report ddr
                                  JOIN diagnostic_report dr ON (dr.id = ddr.diagnostic_report_id)
                                  JOIN health_condition hc ON (hc.id=dr.health_condition_id)
                         WHERE ddr.document_id = d.id AND d.type_id = 6 AND dr.status_id <> '89925002')
               SELECT string_agg('Estudio: ' || s.pt || (CASE WHEN dr.note_id IS NOT NULL AND dr.status_id = '261782000' THEN '.\nResultado: ' || n.description || ' (' || to_char(dr.effective_time, 'dd/MM/yyyy') || ')' ELSE '. (Pendiente)' END),'\n')
               FROM document_diagnostic_report ddr
                        JOIN diagnostic_report dr ON (dr.id=ddr.diagnostic_report_id)
                        JOIN health_condition hc ON (hc.id=dr.health_condition_id)
                        JOIN snomed s ON (s.id=dr.snomed_id)
                        LEFT OUTER JOIN note n ON (dr.note_id = n.id)
               WHERE ddr.document_id = d.id
                 AND d.type_id = 6
                 AND dr.status_id  <> '89925002'
                 AND exists (SELECT 1 FROM temporal_orders t WHERE d.source_id = t.source_id AND dr.snomed_id = t.snomed_id AND dr.health_condition_id = t.health_condition_id)
                 AND NOT exists
                   (SELECT 1
                    FROM document_diagnostic_report ddr2
                             JOIN diagnostic_report dr2 ON (dr2.id = ddr2.diagnostic_report_id)
                    WHERE ddr2.document_id = ddr.document_id
                      AND dr2.id <> dr.id
                      AND dr2.patient_id = dr.patient_id
                      AND dr2.snomed_id = dr.snomed_id
                      AND dr2.health_condition_id = dr2.health_condition_id
                      AND dr2.status_id IN ('261782000', '89925002')) GROUP BY ddr.document_id), '') AS service_request_studies,
      coalesce('Motivos de consulta: '|| (SELECT string_agg(r.description,', ' )
                                          FROM outpatient_consultation oc
                                                   LEFT OUTER JOIN outpatient_consultation_reasons ocr ON (oc.id=ocr.outpatient_consultation_id)
                                                   JOIN reasons r ON (r.id=ocr.reason_id)
                                          WHERE oc.document_id = d.id AND d.type_id = 4 GROUP BY oc.document_id),'') ||
      coalesce('Motivos de consulta: ' || (SELECT string_agg(r.description ,', ')
                                           FROM emergency_care_evolution_note ecen
                                                    JOIN emergency_care_evolution_note_reason ecenr ON (ecenr.emergency_care_evolution_note_id = ecen.id)
                                                    JOIN reasons r ON (r.id  = ecenr.reason_id)
                                           WHERE ecen.document_id = d.id AND d.type_id = 16 GROUP BY ecen.document_id),'') AS consultation_reasons,
      coalesce('Procedimientos sobre tejidos dentales: '|| (SELECT string_agg(DISTINCT('\n' || s.pt || ' en ' || s1.pt),'' )
                                                            FROM document_odontology_procedure dop
                                                                     JOIN odontology_procedure op ON (op.id=dop.odontology_procedure_id)
                                                                     JOIN snomed s ON (s.id=op.snomed_id)
                                                                     LEFT JOIN snomed s1 ON (op.tooth_id = s1.id)
                                                            WHERE dop.document_id = d.id AND d.type_id = 9 GROUP BY dop.document_id),'') AS odontology_procedure,
      coalesce('Hallazgos sobre tejidos dentales: '|| (SELECT string_agg(DISTINCT ('\n' || s.pt || ' en ' || s1.pt),'' )
                                                       FROM document_odontology_diagnostic dod
                                                                JOIN odontology_diagnostic od ON (od.id=dod.odontology_diagnostic_id)
                                                                JOIN snomed s ON (s.id=od.snomed_id)
                                                                LEFT OUTER JOIN snomed s1 ON (s1.id=od.tooth_id)
                                                       WHERE dod.document_id = d.id AND d.type_id = 9 GROUP BY dod.document_id),'') AS odontology_diagnostic,
      coalesce('Cantidad de piezas presentes: ' || (SELECT string_agg(
                                                                   (CASE WHEN oci.permanent_teeth_present IS NOT NULL THEN 'permanentes: ' || oci.permanent_teeth_present ||
                                                                                                                           (CASE WHEN oci.temporary_teeth_present IS NOT NULL THEN ', ' ELSE '' END) ELSE '' END) ||
                                                                   (CASE WHEN oci.temporary_teeth_present IS NOT NULL THEN 'temporales: ' || oci.temporary_teeth_present ELSE '' END), ', ')
                                                    FROM odontology_consultation_indices oci
                                                    WHERE d.type_id = 9 AND d.source_id = oci.odontology_consultation_id), '') AS odontology_pieces,
      coalesce('Dieta: '|| (SELECT string_agg(die.description || '\nFecha indicación: ' || to_char(i.indication_date,'dd/MM/yyyy'), ', ' )
                            FROM document_indication di
                                     JOIN indication i ON (i.id=di.indication_id )
                                     JOIN diet die ON (die.id=i.id)
                            WHERE d.id = di.document_id AND d.type_id = 12 AND i.type_id = 2 GROUP BY di.document_id), '') ||
      coalesce('Otra indicación: '|| (SELECT string_agg
                                             (CASE WHEN oi.other_type IS NOT NULL THEN oit.description || ': ' || oi.other_type ELSE oit.description END || '\nFrecuencia: ' ||
                                              (CASE WHEN dos.period_unit IS NULL THEN 'Sin especificar' ELSE '' END) ||
                                              (CASE WHEN dos.period_unit = 'd' THEN 'Única vez' ELSE '' END) ||
                                              (CASE WHEN dos.period_unit = 'h' THEN 'Cada ' || dos.frequency || ' hs.' ELSE '' END) ||
                                              (CASE WHEN dos.period_unit = 'e' THEN 'Ante evento: ' || dos."event" ELSE '' END) ||
                                              (CASE WHEN oi.description IS NOT NULL THEN '\nNotas: ' || oi.description ELSE '' END) || '\nFecha indicación: ' || to_char(i.indication_date, 'dd/MM/yyyy'),', ' )
                                      FROM document_indication di
                                               JOIN indication i ON (i.id=di.indication_id )
                                               JOIN other_indication oi ON (oi.id=i.id)
                                               JOIN other_indication_type oit ON (oi.other_indication_type_id=oit.id)
                                               LEFT JOIN dosage dos ON (dos.id = oi.dosage_id)
                                      WHERE d.id = di.document_id AND d.type_id = 12 AND i.type_id = 4 GROUP BY di.document_id), '') ||
      coalesce('Fármaco: '|| (SELECT string_agg(s1.pt ||
                                                (CASE WHEN q IS NOT NULL THEN '\nVol/bolsa: ' || q.value || ' ' || q.unit ELSE '' END) ||
                                                (CASE WHEN s2.pt IS NOT NULL THEN '\nDiluyente: ' || s2.pt ELSE '' END) || '\nAdministración: ' ||
                                                (CASE WHEN dos.period_unit = 'd' THEN 'Única vez' ELSE '' END) ||
                                                (CASE WHEN dos.period_unit = 'h' THEN 'Cada ' || dos.frequency || ' hs.' ELSE '' END) ||
                                                (CASE WHEN dos.period_unit = 'e' THEN 'Ante evento: ' || dos."event" ELSE '' END) || '\nRelación con las comidas: ' ||
                                                (CASE WHEN ph.food_relation_id = 0 THEN 'No' ELSE '' END) ||
                                                (CASE WHEN ph.food_relation_id = 1 THEN 'Lejos' ELSE '' END) ||
                                                (CASE WHEN ph.food_relation_id = 2 THEN 'Ayuno' ELSE '' END) ||
                                                '\nDiagnóstico asociado: ' || s.pt || '\nFecha indicación: ' || to_char(i.indication_date, 'dd/MM/yyyy'),'\n' )
                              FROM document_indication di
                                       JOIN indication i ON (i.id=di.indication_id )
                                       JOIN pharmaco ph ON (ph.id=i.id)
                                       JOIN health_condition hc ON (hc.id=ph.health_condition_id)
                                       JOIN snomed s ON (s.id=hc.snomed_id)
                                       JOIN snomed s1 ON (s1.id=ph.snomed_id)
                                       LEFT OUTER JOIN other_pharmaco oph ON (oph.indication_id = i.id)
                                       LEFT OUTER JOIN snomed s2 ON (s2.id = oph.snomed_id)
                                       JOIN dosage dos ON (dos.id=ph.dosage_id)
                                       LEFT OUTER JOIN quantity q ON (q.id=dos.dose_quantity_id)
                              WHERE d.id = di.document_id AND d.type_id = 12 AND i.type_id = 1 GROUP BY di.document_id),'') ||
      coalesce('Plan parenteral: '||(SELECT string_agg(s.pt ||
                                                       (CASE WHEN q IS NOT NULL THEN '\nVol/bolsa: ' || q.value || ' ' || q.unit ELSE '' END) ||
                                                       (CASE WHEN f.duration IS NOT NULL THEN '\nDuración: ' || to_char(f.duration, 'HH24:MI') || ' hs' ELSE '' END) ||
                                                       '\nFlujo: ' || f.flow_ml_hour || ' ml/h | ' || f.flow_drops_hour || ' hp. Gotas' ||
                                                       (CASE WHEN f.daily_volume IS NOT NULL THEN '\nVol/Día: ' || f.daily_volume || 'ml' ELSE '' END) ||
                                                       (CASE WHEN s1.pt IS NOT NULL THEN '\nFármaco: ' || s1.pt ELSE '' END) || '\nFecha indicación: ' || to_char(i.indication_date, 'dd/MM/yyyy') ,', ' )
                                     FROM document_indication di
                                              JOIN indication i ON (i.id=di.indication_id )
                                              JOIN parenteral_plan pp ON (pp.id=i.id)
                                              JOIN snomed s ON (pp.snomed_id=s.id)
                                              JOIN dosage dos ON (pp.dosage_id=dos.id)
                                              JOIN frequency f ON (f.id = pp.frequency_id)
                                              LEFT OUTER JOIN quantity q ON (q.id=dos.dose_quantity_id )
                                              LEFT OUTER JOIN other_pharmaco oph ON (oph.indication_id=pp.id)
                                              LEFT OUTER JOIN snomed s1 ON (s1.id=oph.snomed_id)
                                     WHERE d.id = di.document_id AND d.type_id = 12 AND i.type_id = 3 GROUP BY di.document_id),'') ||
      coalesce('\nEstado: ' || (SELECT string_agg(
                                               (CASE WHEN i.status_id = 1 THEN 'Indicada' ELSE '' END) ||
                                               (CASE WHEN i.status_id = 2 THEN 'Suspendida' ELSE '' END) ||
                                               (CASE WHEN i.status_id = 3 THEN 'En progreso' ELSE '' END) ||
                                               (CASE WHEN i.status_id = 4 THEN 'Completada' ELSE '' END) ||
                                               (CASE WHEN i.status_id = 5 THEN 'Rechazada' ELSE '' END), ', ')
                                FROM document_indication di
                                         JOIN indication i ON (i.id = di.indication_id)
                                WHERE di.document_id = d.id AND d.type_id = 12 GROUP BY di.document_id),'') AS indication,
      coalesce('Detalles de referencia solicitada: ' || (SELECT string_agg(
                                                                        (CASE WHEN re.care_line_id IS NOT NULL THEN '\nLinea de cuidado: ' || cl.description ELSE '' END) || '\nEspecialidad: ' || clinical_specialties ||
                                                                        (CASE WHEN re.reference_note_id IS NOT NULL THEN '\nObservaciones: ' || rn.description ELSE '' END), ', ')
                                                         FROM counter_reference cr
                                                                  JOIN reference re ON (re.id = cr.reference_id)
                                                                  JOIN (
                                                             SELECT re2.id, STRING_AGG(cs."name" , ', ') AS clinical_specialties
                                                             FROM reference re2
                                                                      JOIN reference_clinical_specialty rcs ON (rcs.reference_id = re2.id)
                                                                      JOIN clinical_specialty cs ON (cs.id = rcs.clinical_specialty_id)
                                                             GROUP BY re2.id
                                                         ) AS reference_clinical_specialties ON (reference_clinical_specialties.id = re.id)
                                                                  LEFT OUTER JOIN care_line cl ON (cl.id = re.care_line_id)
                                                                  LEFT OUTER JOIN reference_note rn ON (rn.id = re.reference_note_id)
                                                         WHERE cr.id = d.source_id AND d.type_id = 11), '') AS reference_counter_reference,
      coalesce('Tipo de cierre: ' || (SELECT string_agg(
                                                     (CASE WHEN cr.closure_type_id = 1 THEN 'Continúa en observación' ELSE '' END) ||
                                                     (CASE WHEN cr.closure_type_id = 2 THEN 'Inicia tratamiento en centro de referencia' ELSE '' END) ||
                                                     (CASE WHEN cr.closure_type_id = 3 THEN 'Requiere estudios complementarios' ELSE '' END) ||
                                                     (CASE WHEN cr.closure_type_id = 4 THEN 'Contrarreferencia' ELSE '' END), ', ')
                                      FROM counter_reference cr
                                      WHERE cr.id = d.source_id AND d.type_id = 11), '') ||
      coalesce((SELECT string_agg('\nDescripción de cierre: ' || n.description, ', ' )
                FROM note n
                WHERE n.id = d.evolution_note_id AND d.type_id = 11), '') AS counter_reference_closure,
      coalesce((SELECT string_agg(tc."name", ', ')
                FROM document_triage dt
                         JOIN triage t ON (t.id = dt.triage_id)
                         JOIN triage_category tc ON (tc.id = t.triage_category_id)
                WHERE dt.document_id = d.id AND d.type_id = 15 GROUP BY d.id),'') AS triage_level,
      coalesce('\nEvolución: '|| (SELECT string_agg(n.description, ', ' )
                                  FROM note n
                                  WHERE n.id = d.evolution_note_id AND d.type_id = 3), '') ||
      coalesce('\nEnfermedad actual: '|| (SELECT string_agg(n.description, ', ' )
                                          FROM note n
                                          WHERE n.id = d.current_illness_note_id AND d.type_id IN (1, 2)), '') ||
      coalesce('\nIndicaciones al alta: '|| (SELECT string_agg(n.description, ', ' )
                                             FROM note n
                                             WHERE n.id = d.indications_note_id AND d.type_id = 3), '') ||
      coalesce('\nResumen de estudios y procedimientos realizados: '|| (SELECT string_agg(n.description, ', ' )
                                                                        FROM note n
                                                                        WHERE n.id = d.studies_summary_note_id AND d.type_id = 3), '') ||
      coalesce('\nExamen físico: '|| (SELECT string_agg(n.description, ', ' )
                                      FROM note n
                                      WHERE n.id = d.physical_exam_note_id AND d.type_id IN (1, 2, 3)), '') ||
      coalesce('\nResumen de estudios y procedimientos realizados: '|| (SELECT string_agg(n.description, ', ' )
                                                                        FROM note n
                                                                        WHERE n.id = d.studies_summary_note_id AND d.type_id IN (1, 2)), '') ||
      coalesce('\nEvolución: '|| (SELECT string_agg(n.description, ', ' )
                                  FROM note n
                                  WHERE n.id = d.evolution_note_id AND d.type_id IN (1, 2)), '') ||
      coalesce('\nImpresión clínica y plan: '|| (SELECT string_agg(n.description, ', ' )
                                                 FROM note n
                                                 WHERE n.id = d.clinical_impression_note_id AND d.type_id IN (1, 2)), '') ||
      coalesce('\nOtras observaciones: '|| (SELECT string_agg(n.description, ',' )
                                            FROM note n
                                            WHERE n.id = d.other_note_id AND d.type_id IN (1, 2, 3)), '') ||
      coalesce('Evolución: ' || (SELECT string_agg(n.description, ', ')
                                 FROM note n
                                 WHERE n.id = d.other_note_id AND d.type_id IN (4, 15, 16)), '') ||
      coalesce('Evolución: '|| (SELECT string_agg(n.description, ', ' )
                                FROM note n
                                WHERE n.id = d.evolution_note_id AND d.type_id IN (9, 10, 13)), '') AS notes,
      coalesce('Cirugía propuesta: ' || (SELECT string_agg(s.pt,', ')
                                         FROM document_procedure dp
                                                  LEFT OUTER JOIN procedures p ON (p.id = dp.procedure_id)
                                                  LEFT OUTER JOIN snomed s ON (s.id = p.snomed_id)
                                         WHERE dp.document_id = d.id AND d.type_id IN (20) AND p.procedure_type_id = 2
                                         GROUP BY dp.document_id), '') AS surgery_procedures,
      coalesce('Evaluación clínica anestésica: ' ||
               (SELECT '¿El paciente se realizó algún tipo de anestesia previamente? ' ||
                       CASE WHEN ah.state_id = 1 THEN 'Si (' || CASE WHEN ah.zone_id = 1 THEN 'Regional' ELSE CASE WHEN ah.zone_id = 2 THEN 'General' ELSE 'Ambas' END END || ')'
                            ELSE CASE WHEN ah.state_id = 2 THEN 'No' ELSE 'No puede contestar' END END
                FROM anesthetic_history ah
                WHERE ah.document_id = d.id AND d.type_id IN (20)), '') AS anesthetic_history,
      coalesce('Premedicación e ingesta alimentaria: ' ||
               (WITH t AS
                         (SELECT coalesce((SELECT string_agg((s.pt) ||
                                                             ' (vía: ' || v.description ||
                                                             CASE WHEN n.description IS NOT NULL THEN ' - ' || n.description ELSE '' END ||
                                                             ', dosis: ' || q.value || q.unit || ', fecha: ' || to_char(dg.start_date, 'dd/MM/yyyy') || ' - ' || to_char(dg.start_date, 'HH24:MI') ||
                                                             ' hs.)', ', ')
                                           FROM anesthetic_substance a
                                                    JOIN snomed s ON (s.id = a.snomed_id)
                                                    JOIN dosage dg ON (a.dosage_id = dg.id)
                                                    JOIN quantity q ON (dg.dose_quantity_id = q.id)
                                                    JOIN via v ON (v.id = a.via_id)
                                                    LEFT JOIN note n ON (a.via_note_id = n.id)
                                           WHERE a.document_id = d.id AND a.type_id = 1 AND d.type_id IN (20) GROUP BY a.document_id),'') ||
                                 coalesce((SELECT CASE WHEN pd.food_intake_date IS NOT NULL OR pd.food_intake IS NOT NULL THEN '\n- Última ingesta de comida: ' ELSE '' END ||
                                                  CASE WHEN pd.food_intake_date IS NOT NULL THEN to_char(pd.food_intake_date, 'dd/MM/yyyy') ELSE '' END ||
                                                  CASE WHEN pd.food_intake_date IS NOT NULL AND pd.food_intake IS NOT NULL THEN ' - ' ELSE '' END ||
                                                  CASE WHEN pd.food_intake IS NOT NULL THEN to_char(pd.food_intake, 'HH24:MI') || ' hs' ELSE '' END
                                           FROM procedure_description pd
                                           WHERE pd.document_id = d.id AND d.type_id IN (20) AND pd.food_intake IS NOT NULL),'') AS result
                         )
                SELECT CASE WHEN t.result <> '' THEN t.result END FROM t GROUP BY t.result), '') AS pre_medications,
      coalesce('Antecedentes: ' ||
               (WITH t AS
                       (SELECT coalesce((SELECT string_agg(s.pt, ', ')
                                            FROM document_health_condition dhc
                                                     JOIN health_condition hc ON (dhc.health_condition_id = hc.id)
                                                     JOIN snomed s ON (s.id = hc.snomed_id)
                                            WHERE dhc.document_id = d.id AND d.type_id IN (20) AND hc.problem_id = '00000003' GROUP BY dhc.document_id),'') ||
                               coalesce((SELECT CASE WHEN n.description IS NOT NULL THEN '\n- Observaciones: \n' || n.description ELSE '' END ||
                                       CASE WHEN pd.asa IS NOT NULL THEN '\n- ASA: ' || pd.asa ELSE '' END
                                    FROM procedure_description pd
                                             LEFT JOIN note n ON (pd.note_id = n.id)
                                    WHERE pd.document_id = d.id AND d.type_id IN (20)),'') AS result
                      )
                SELECT CASE WHEN t.result <> '' THEN t.result END FROM t GROUP BY t.result), '') AS histories,
      coalesce('Plan anestésico: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (vía: ' || v.description ||
                                  CASE WHEN n.description IS NOT NULL THEN ' - ' || n.description ELSE '' END ||
                                  ', dosis: ' || q.value || q.unit || ', fecha: ' || to_char(dg.start_date, 'dd/MM/yyyy') || ' - ' || to_char(dg.start_date, 'HH24:MI') ||
                                  ' hs.)', ', ')
                FROM anesthetic_substance a
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                         JOIN via v ON (v.id = a.via_id)
                         LEFT JOIN note n ON (a.via_note_id = n.id)
                WHERE a.document_id = d.id AND a.type_id = 2 AND d.type_id IN (20) GROUP BY a.document_id), '') AS anesthetic_plans,
      coalesce('Técnica analgésica: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (zona de inyección: ' || n_inj.description ||
                                  ', dosis: ' || q.value || q.unit ||
                                  ', cateter: ' || CASE WHEN at.catheter = TRUE THEN 'Si' ELSE 'No' END ||
                                  CASE WHEN n_cat.description IS NOT NULL THEN ' - ' || n_cat.description END || ')', ', ')
                FROM anesthetic_substance a
                         JOIN analgesic_technique at ON (a.id = at.anesthetic_substance_id)
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                         LEFT JOIN note n_inj ON (at.injection_note_id = n_inj.id)
                         LEFT JOIN note n_cat ON (at.catheter_note_id = n_cat.id)
                WHERE a.document_id = d.id AND a.type_id = 3 AND d.type_id IN (20) GROUP BY a.document_id), '') AS analgesic_techniques,
      coalesce('Técnica anestésica: ' ||
               (SELECT string_agg((s.pt) ||
                                  CASE WHEN at.technique_id IS NOT NULL OR at.tracheal_intubation IS NOT NULL OR at.breathing_id IS NOT NULL OR at.circuit_id IS NOT NULL THEN ' (' END ||
                                  CASE WHEN at.technique_id IS NOT NULL THEN 'técnica: ' || CASE WHEN at.technique_id = 1 THEN 'Inhalatoria' ELSE CASE WHEN at.technique_id = 2 THEN 'Endovenosa' ELSE 'Ambas' END END ELSE '' END ||
                                  CASE WHEN at.technique_id IS NOT NULL AND (at.tracheal_intubation IS NOT NULL OR at.breathing_id IS NOT NULL OR at.circuit_id IS NOT NULL) THEN ', ' END ||
                                  CASE WHEN at.tracheal_intubation IS NOT NULL THEN 'intubación traqueal: ' || CASE WHEN at.tracheal_intubation = TRUE THEN 'Si' ELSE 'No' END ELSE '' END ||
                                  CASE WHEN (at.technique_id IS NOT NULL OR at.tracheal_intubation IS NOT NULL) AND (at.breathing_id IS NOT NULL OR at.circuit_id IS NOT NULL) THEN ', ' END ||
                                  CASE WHEN at.breathing_id IS NOT NULL THEN 'respiración: ' || CASE WHEN at.breathing_id = 1 THEN 'Espontánea' ELSE CASE WHEN at.breathing_id = 2 THEN 'Manual' ELSE 'Asistida' END END ELSE '' END ||
                                  CASE WHEN (at.technique_id IS NOT NULL OR at.tracheal_intubation IS NOT NULL OR at.breathing_id IS NOT NULL) AND at.circuit_id IS NOT NULL THEN ', ' END ||
                                  CASE WHEN at.circuit_id IS NOT NULL THEN 'circuito: ' || CASE WHEN at.circuit_id = 1 THEN 'Abierto' ELSE CASE WHEN at.technique_id = 2 THEN 'Cerrado' ELSE 'Circular' END END ELSE '' END ||
                                  CASE WHEN at.technique_id IS NOT NULL OR at.tracheal_intubation IS NOT NULL OR at.breathing_id IS NOT NULL OR at.circuit_id IS NOT NULL THEN ')' END, ', ')
                FROM anesthetic_technique at
                         JOIN snomed s ON (s.id = at.snomed_id)
                WHERE at.document_id = d.id AND d.type_id IN (20) GROUP BY at.document_id), '') AS anesthetic_techniques,
      coalesce('Administración de líquidos: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (dosis: ' || q.value || q.unit ||')', ', ')
                FROM anesthetic_substance a
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                WHERE a.document_id = d.id AND a.type_id = 4 AND d.type_id IN (20) GROUP BY a.document_id), '') AS fluid_administrations,
      coalesce('Agentes anestésicos: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (vía: ' || v.description ||
                                  CASE WHEN n.description IS NOT NULL THEN ' - ' || n.description ELSE '' END ||
                                  ', dosis: ' || q.value || q.unit || ', fecha: ' || to_char(dg.start_date, 'dd/MM/yyyy') || ' - ' || to_char(dg.start_date, 'HH24:MI') ||
                                  ' hs.)', ', ')
                FROM anesthetic_substance a
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                         JOIN via v ON (v.id = a.via_id)
                         LEFT JOIN note n ON (a.via_note_id = n.id)
                WHERE a.document_id = d.id AND a.type_id = 5 AND d.type_id IN (20) GROUP BY a.document_id), '') AS anesthetic_agents,
      coalesce('Drogas no anestésicas: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (vía: ' || v.description ||
                                  CASE WHEN n.description IS NOT NULL THEN ' - ' || n.description ELSE '' END ||
                                  ', dosis: ' || q.value || q.unit || ', fecha: ' || to_char(dg.start_date, 'dd/MM/yyyy') || ' - ' || to_char(dg.start_date, 'HH24:MI') ||
                                  ' hs.)', ', ')
                FROM anesthetic_substance a
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                         JOIN via v ON (v.id = a.via_id)
                         LEFT JOIN note n ON (a.via_note_id = n.id)
                WHERE a.document_id = d.id AND a.type_id = 6 AND d.type_id IN (20) GROUP BY a.document_id), '') AS non_anesthetic_drugs,
      coalesce('Procedimientos anestésicos intraquirúrgicos: ' ||
               (SELECT CASE WHEN pd.venous_access IS NOT NULL THEN '\n- Acceso venoso: ' || CASE WHEN pd.venous_access = TRUE THEN 'Si' ELSE 'No' END END ||
                       CASE WHEN pd.nasogastric_tube IS NOT NULL THEN '\n- Sonda nasogastrica: ' || CASE WHEN pd.nasogastric_tube = TRUE THEN 'Si' ELSE 'No' END END ||
                       CASE WHEN pd.urinary_catheter IS NOT NULL THEN '\n- Sonda vesical: ' || CASE WHEN pd.urinary_catheter = TRUE THEN 'Si' ELSE 'No' END END
                FROM procedure_description pd
                WHERE pd.document_id = d.id
                  AND d.type_id IN (20)), '') AS intrasurgical_anesthetic_procedures,
      coalesce('Profilaxis antibiótica: ' ||
               (SELECT string_agg((s.pt) ||
                                  ' (vía: ' || v.description ||
                                  CASE WHEN n.description IS NOT NULL THEN ' - ' || n.description ELSE '' END ||
                                  ', dosis: ' || q.value || q.unit || ', fecha: ' || to_char(dg.start_date, 'dd/MM/yyyy') || ' - ' || to_char(dg.start_date, 'HH24:MI') ||
                                  ' hs.)', ', ')
                FROM anesthetic_substance a
                         JOIN snomed s ON (s.id = a.snomed_id)
                         JOIN dosage dg ON (a.dosage_id = dg.id)
                         JOIN quantity q ON (dg.dose_quantity_id = q.id)
                         JOIN via v ON (v.id = a.via_id)
                         LEFT JOIN note n ON (a.via_note_id = n.id)
                WHERE a.document_id = d.id AND a.type_id = 7 AND d.type_id IN (20) GROUP BY a.document_id), '') AS antibiotic_prophylaxis,
      coalesce('Signos vitales durante la anestesia: ' ||
               (WITH t AS
                         (SELECT coalesce((SELECT CASE WHEN pd.anesthesia_start_date IS NOT NULL OR pd.anesthesia_start_time IS NOT NULL THEN '\n- Comienzo anestesia: ' ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_start_date IS NOT NULL THEN to_char(pd.anesthesia_start_date, 'dd/MM/yyyy') ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_start_date IS NOT NULL AND pd.anesthesia_start_time IS NOT NULL THEN ' - ' ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_start_time IS NOT NULL THEN to_char(pd.anesthesia_start_time, 'HH24:MI') || ' hs' ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_end_date IS NOT NULL OR pd.anesthesia_end_time IS NOT NULL THEN '\n- Fin anestesia: ' ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_end_date IS NOT NULL THEN to_char(pd.anesthesia_end_date, 'dd/MM/yyyy') ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_end_date IS NOT NULL AND pd.anesthesia_end_time IS NOT NULL THEN ' - ' ELSE '' END ||
                                                  CASE WHEN pd.anesthesia_end_time IS NOT NULL THEN to_char(pd.anesthesia_end_time, 'HH24:MI') || ' hs' ELSE '' END ||
                                                  CASE WHEN pd.surgery_start_date IS NOT NULL OR pd.surgery_start_time IS NOT NULL THEN '\n- Comienzo cirugía: ' ELSE '' END ||
                                                  CASE WHEN pd.surgery_start_date IS NOT NULL THEN to_char(pd.surgery_start_date, 'dd/MM/yyyy') ELSE '' END ||
                                                  CASE WHEN pd.surgery_start_date IS NOT NULL AND pd.surgery_start_time IS NOT NULL THEN ' - ' ELSE '' END ||
                                                  CASE WHEN pd.surgery_start_time IS NOT NULL THEN to_char(pd.surgery_start_time, 'HH24:MI') || ' hs' ELSE '' END ||
                                                  CASE WHEN pd.surgery_end_date IS NOT NULL OR pd.surgery_end_time IS NOT NULL THEN '\n- Fin cirugía: ' ELSE '' END ||
                                                  CASE WHEN pd.surgery_end_date IS NOT NULL THEN to_char(pd.surgery_end_date, 'dd/MM/yyyy') ELSE '' END ||
                                                  CASE WHEN pd.surgery_end_date IS NOT NULL AND pd.surgery_end_time IS NOT NULL THEN ' - ' ELSE '' END ||
                                                  CASE WHEN pd.surgery_end_time IS NOT NULL THEN to_char(pd.surgery_end_time, 'HH24:MI') || ' hs' ELSE '' END
                                           FROM procedure_description pd
                                           WHERE pd.document_id= d.id AND d.type_id IN (20)),'') ||
                                 coalesce((SELECT '\hr' || string_agg('Medición' ||
                                                                      ' (' || to_char(mp.date, 'dd/MM/yyyy') || ' - ' || to_char(mp.time, 'HH24:MI') || ' hs.)\n' ||
                                                                      'TA Max: ' || CASE WHEN mp.blood_pressure_max IS NOT NULL THEN mp.blood_pressure_max || 'mmHg' ELSE '-' END || ' ' ||
                                                                      'TA Min: ' || CASE WHEN mp.blood_pressure_min IS NOT NULL THEN mp.blood_pressure_min || 'mmHg' ELSE '-' END || ' ' ||
                                                                      'Pulso: ' || CASE WHEN mp.blood_pulse IS NOT NULL THEN mp.blood_pulse || 'lat/min' ELSE '-' END || ' ' ||
                                                                      'Saturación O2: ' || CASE WHEN mp.o2_saturation IS NOT NULL THEN mp.o2_saturation || '%' ELSE '-' END || ' ' ||
                                                                      'End Tidal CO2: ' || CASE WHEN mp.co2_end_tidal IS NOT NULL THEN mp.co2_end_tidal || '' ELSE '-' END, '\n\hr')
                                           FROM measuring_point mp
                                           WHERE mp.document_id = d.id
                                           GROUP BY mp.document_id),'') AS result
                         )
                SELECT CASE WHEN t.result <> '' THEN t.result END FROM t GROUP BY t.result), '') AS vital_signs_anesthesia,
      coalesce('Estado al finalizar la anestesia: ' ||
               (SELECT CASE WHEN dpas.intentional_sensitivity IS NOT NULL THEN '\n- Sensibilidad dolorosa: ' || CASE WHEN dpas.intentional_sensitivity = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.corneal_reflex IS NOT NULL THEN '\n- Reflejo corneal: ' || CASE WHEN dpas.corneal_reflex = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.obey_orders IS NOT NULL THEN '\n- Obedece órdenes: ' || CASE WHEN dpas.obey_orders = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.talk IS NOT NULL THEN '\n- Conversa: ' || CASE WHEN dpas.talk = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.respiratory_depression IS NOT NULL THEN '\n- Depresión respiratoria: ' || CASE WHEN dpas.respiratory_depression = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.circulatory_depression IS NOT NULL THEN '\n- Depresión circulatoria: ' || CASE WHEN dpas.circulatory_depression = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.vomiting IS NOT NULL THEN '\n- Vómitos: ' || CASE WHEN dpas.vomiting = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.curated IS NOT NULL THEN '\n- Curarizado: ' || CASE WHEN dpas.curated = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.tracheal_cannula IS NOT NULL THEN '\n- Cánula traqual: ' || CASE WHEN dpas.tracheal_cannula = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.pharyngeal_cannula IS NOT NULL THEN '\n- Cánula faringea: ' || CASE WHEN dpas.pharyngeal_cannula = TRUE THEN 'Si' ELSE 'No' END  ELSE '' END ||
                       CASE WHEN dpas.internment IS NOT NULL THEN '\n- Se interna: ' || CASE WHEN dpas.internment = TRUE THEN 'Si' ELSE 'No' END
                           || CASE WHEN dpas.internment_place_id IS NOT NULL THEN ' - ' || CASE WHEN dpas.internment_place_id = 1 THEN 'En piso' ELSE 'En unidad de terapia intensiva' END ELSE '' END  ELSE '' END ||
                       CASE WHEN n.description IS NOT NULL THEN '\n- Observaciones: \n' || n.description ELSE '' END || '\n'
                FROM document_post_anesthesia_status dpas
                         LEFT JOIN note n ON (dpas.note_id = n.id)
                WHERE dpas.document_id = d.id
               ), '') AS post_anesthesia_status

FROM document AS d
        JOIN document_type dt ON (d.type_id=dt.id)
        JOIN institution i ON (i.id=d.institution_id)
        LEFT OUTER JOIN clinical_specialty cs ON (d.clinical_specialty_id=cs.id)
WHERE d.deleted=FALSE AND d.status_id = '445665009'
);
