BEGIN;
DO
$func$
BEGIN
/*#####################

Create v_clinic_history

#####################*/
CREATE VIEW v_clinic_history AS
(
    select d.id as id,
    d.source_id as source_id,
    d.patient_id as patient_id,
    d.source_type_id as source_type_id,
    d.created_on,
    d.created_by,
    d.type_id as document_type_id,
    dt.description as document_type,
    cs.name as clinical_specialty,
    i.id as institution_id,
    i.name as institution,
    d.patient_age_period as patient_age_period,
    (select sr.source_type_id from service_request sr where d.type_id = 6 and d.source_id = sr.id) as request_source_type_id,
    (select sr.source_id from service_request sr where d.type_id = 6 and d.source_id = sr.id) as request_source_id,
    (select MIN(ie.entry_date)
    from internment_episode ie
    left join service_request sr on (sr.source_id = ie.id)
    where (d.source_id = ie.id and d.source_type_id = 0 and d.type_id <> 6)
    or (d.type_id = 6 and sr.source_type_id = 0 and d.source_id = sr.id and sr.source_id = ie.id)) as internment_start_date,
    (select MAX(pd.administrative_discharge_date)
    from internment_episode ie
    left join service_request sr on (sr.source_id = ie.id)
    left join patient_discharge pd on (pd.internment_episode_id = ie.id)
    where (d.source_id = ie.id and d.source_type_id = 0 and d.type_id <> 6)
    or (d.type_id = 6 and sr.source_type_id = 0 and d.source_id = sr.id and sr.source_id = ie.id)) as internment_end_date,
    (select MIN(ece.created_on)
    from emergency_care_episode ece
    left join service_request sr on (sr.source_id = ece.id)
    where (d.source_id = ece.id and d.source_type_id = 4 and d.type_id <> 6)
    or (d.type_id = 6 and sr.source_type_id = 4 and d.source_id = sr.id and sr.source_id = ece.id)) as emergency_care_start_date,
    (select MAX(ecd.administrative_discharge_on)
    from emergency_care_episode ece
    left join service_request sr on (sr.source_id = ece.id)
    join emergency_care_discharge ecd on (ecd.emergency_care_episode_id = ece.id)
    where (d.source_id = ece.id and d.source_type_id = 4 and d.type_id <> 6)
    or (d.type_id = 6 and sr.source_type_id = 4 and d.source_id = sr.id and sr.source_id = ece.id)) as emergency_care_end_date,
    (select (case when(
    (select count(*) filter (where dr.status_id = '1')
    from document_diagnostic_report ddr
    join diagnostic_report dr on (dr.id = ddr.diagnostic_report_id)
    where ddr.document_id = d.id and d.type_id= 6 group by ddr.document_id)
    =
    (select count(*) filter (where dr.status_id = '261782000' or dr.status_id = '89925002')
    from document_diagnostic_report ddr
    join diagnostic_report dr
    on (dr.id = ddr.diagnostic_report_id)
    where ddr.document_id = d.id and d.type_id = 6 group by ddr.document_id))
    then
    (select MAX(dr.created_on)
    from document_diagnostic_report ddr
    join diagnostic_report dr on (dr.id = ddr.diagnostic_report_id) where ddr.document_id = d.id and dr.status_id = '261782000')
    end)) as service_request_end_date,
    (select MAX(ms.updated_on)
    from document_medicamention_statement dms
    join medication_statement ms on (ms.id = dms.medication_statement_id)
    where dms.document_id = d.id and d.type_id = 5 and ms.status_id in ('255594003', '6155003', '385655000')) as medication_end_date,
    COALESCE('Diagnósticos: ' || (select string_agg((case when hc.main then 'Principal: ' || s.pt else 'Otro: ' || s.pt end) ||
    (case when hc.status_id in ('723506003', '277022003', '73425007') then '|(Eliminado el día ' || to_char(hc.start_date ,'dd-mm-yyyy') || ')' else '' end), '| ')
    from document_health_condition dhc
    join health_condition hc on (hc.id = dhc.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where dhc.document_id = d.id and d.type_id in (1, 2, 3, 13, 16) and hc.problem_id = '439401001' group by dhc.document_id), '') ||
    coalesce(case when d.type_id = 10 then 'Problema asociado: ' else 'Problemas: ' end ||
    (select string_agg(s.pt || (case when hc.status_id = '-55607006' then '|(Crónico)' else '' end) ||
    (case when hc.start_date is not null and d.type_id <> 10 then '|(desde ' || to_char(hc.start_date ,'dd-mm-yyyy') else '' end) ||
    (case when hc.inactivation_date is not null and d.type_id <> 10 then ' hasta ' || to_char(hc.inactivation_date, 'dd-mm-yyyy') else '' end) ||
    (case when perl.description is not null and d.type_id <> 10 then ', motivo: ' || perl.description else '' end) ||
    (case when n.description is not null and d.type_id <> 10 then ', observaciones: ' || n.description else '' end) ||
    (case when hc.start_date is not null and d.type_id <> 10 then ')' else '' end), '| ')
    from document_health_condition dhc
    join health_condition hc on (hc.id = dhc.health_condition_id)
    left join note n on (hc.note_id = n.id)
    left join problem_error_reason per on (hc.id = per.health_condition_id)
    left join problem_error_reasons_list perl on (per.reason_id = perl.id)
    join snomed s on (s.id = hc.snomed_id)
    where dhc.document_id = d.id and d.type_id in (4, 7, 10) and hc.problem_id in ('55607006', '-55607006') group by dhc.document_id), '') ||
    COALESCE('Problemas asociados: ' || (select string_agg(r.description, '| ')
    from emergency_care_episode_reason ecer
    join reasons r on (ecer.reason_id=r.id)
    where d.type_id = 15 and d.source_id = ecer.emergency_care_episode_id), '') ||
    COALESCE('Motivos de consulta: ' || (select string_agg(or1.description , '| ')
    from odontology_consultation_reason ocr
    join odontology_reason or1 on (or1.id = ocr.reason_id)
    where ocr.odontology_consultation_id = d.source_id and d.type_id = 9 ), '') ||
    COALESCE('Problemas: ' || (select string_agg(distinct s.pt, '| ')
    from document_medicamention_statement dms
    join medication_statement ms on (ms.id = dms.medication_statement_id)
    left outer join health_condition hc on (hc.id = ms.health_condition_id)
    left outer join snomed s on (s.id = hc.snomed_id)
    where dms.document_id = d.id and d.type_id in (5, 14) group by dms.document_id), '') ||
    COALESCE('Problemas: ' || (select string_agg(s.pt, '| ' )
    from document_indication di
    join indication i on (i.id = di.indication_id )
    join pharmaco ph on (ph.id = i.id)
    join health_condition hc on (hc.id = ph.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where d.id = di.document_id and d.type_id = 12 group by di.document_id), '') ||
    COALESCE('Diagnostico asociado: ' || (select string_agg(distinct s.pt, '| ')
    from document_diagnostic_report ddr
    join diagnostic_report dr on (dr.id = ddr.diagnostic_report_id)
    join health_condition hc on (hc.id = dr.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where ddr.document_id = d.id and d.type_id = 6 group by ddr.document_id), '') ||
    COALESCE('Problemas asociados: ' || (select string_agg(distinct s.pt, '| ')
    from counter_reference cr
    join reference_health_condition rhc on (cr.reference_id = rhc.reference_id)
    join health_condition hc on (hc.id = rhc.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where d.source_id = cr.id and d.type_id = 11), '') as problems,
    COALESCE('Antecedentes familiares: ' || COALESCE ((select string_agg(s.pt || (case when hc.start_date is not null then ' (desde ' || to_char(hc.start_date, 'dd-mm-yyyy') || ')' else '' end), ', ')
    from document_health_condition dhc
    join health_condition hc on (hc.id = dhc.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where dhc.document_id = d.id
    and d.type_id in (1, 3, 4, 16)
    and hc.problem_id = '57177007'
    group by dhc.document_id),
    (SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
    FROM document_referable_concept drc
    WHERE drc.document_id = d.id
    AND drc.referable_concept_id = 3)), '') as family_record,
    COALESCE(case when d.type_id = 9 then 'Otros diagnósticos y antecedentes personales: ' else 'Antecedentes personales: ' end ||
    COALESCE((select string_agg(s.pt ||
    (case when hc.start_date is not null then ' (desde ' || to_char(hc.start_date ,'dd-mm-yyyy') else '' end) ||
    (case when hc.inactivation_date is not null then ' hasta ' || to_char(hc.inactivation_date, 'dd-mm-yyyy') else '' end) ||
    (case when pht.description is not null then ', tipo: ' || pht.description else '' end) ||
    (case when n.description is not null then ', observaciones: ' || n.description else '' end) ||
    (case when hc.start_date is not null then ')' else '' end), '| ')
    from document_health_condition dhc
    join health_condition hc on (hc.id = dhc.health_condition_id)
    left join note n on (hc.note_id = n.id)
    left join personal_history ph ON (hc.id = ph.health_condition_id)
    left join personal_history_type pht ON (ph.type_id = pht.id)
    join snomed s on (s.id = hc.snomed_id)
    where dhc.document_id = d.id
    and hc.problem_id in ('00000002')
    group by dhc.document_id),
    (
    SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
    FROM document_referable_concept drc
    WHERE drc.document_id = d.id
    AND drc.referable_concept_id = 2
    )), '') as personal_record,
    COALESCE('Procedimientos: ' || (select string_agg(case when p.performed_date is not null then s.pt || ' (' || to_char(p.performed_date, 'dd-mm-yyyy') || ')' else s.pt END ,', ')
    from document_procedure dp
    left outer join "procedures" p  on (p.id=dp.procedure_id)
    left outer join snomed s on (s.id=p.snomed_id)
    where dp.document_id = d.id and d.type_id in (1, 2, 3, 4, 9, 10, 11, 16) group by dp.document_id),'') as procedures,
    COALESCE((select string_agg(s.pt ||
    (case when dos.period_unit = 'd' then '\nUna aplicación por día' else '' end) ||
    (case when dos.period_unit = 'h' then '\nUna aplicación cada ' || dos.frequency || 'hs' else '' end) ||
    (case when dos.chronic = true then ', uso crónico' else '' end) ||
    (case when dos.duration is not null then ' durante ' || dos.duration || ' días' else '' end) ||
    (case when ms.note_id is not null then '\nObservaciones: ' || n.description else '' end) ||
    (case when hc.snomed_id is not null then '\nProblema asociado: ' || s2.pt else '' end) ||
    (case when ms.status_id = '6155003' then '\nFinalizada el día ' || to_char(ms.updated_on, 'dd-mm-yyyy') else '' end) ||
    (case when ms.status_id = '385655000' then '\nSuspendida el día ' || to_char(ms.updated_on, 'dd-mm-yyyy') else '' end) , '\n\n')
    from document_medicamention_statement dms
    join medication_statement ms  on (ms.id=dms.medication_statement_id)
    join medication_statement_status mss on (ms.status_id=mss.id)
    join snomed s on (s.id=ms.snomed_id)
    left outer join note n on (n.id = ms.note_id)
    left outer join health_condition hc on (hc.id=ms.health_condition_id)
    left outer join snomed s2 on (s2.id = hc.snomed_id)
    left outer join dosage dos on (ms.dosage_id=dos.id)
    where dms.document_id = d.id and d.type_id in (5, 14) group by dms.document_id),'') ||
    COALESCE('Medicación habitual: '|| (select string_agg(s.pt ||
    (case when ms.note_id is not null then ' (' || n.description || ')' else '' end) ||
    (case when ms.status_id = '6155003' then ' (finalizada el día ' || to_char(ms.updated_on, 'dd-mm-yyyy') || ')' else '' end) ||
    (case when ms.status_id = '385655000' then ' (suspendida el día ' || to_char(ms.updated_on, 'dd-mm-yyyy') || ')' else '' end), '. ')
    from document_medicamention_statement dms
    join medication_statement ms  on (ms.id=dms.medication_statement_id)
    join medication_statement_status mss on (ms.status_id=mss.id)
    join snomed s on (s.id=ms.snomed_id)
    left outer join note n on (n.id = ms.note_id)
    where dms.document_id = d.id and d.type_id in (1, 3, 4, 9, 11, 16) group by dms.document_id),'') as medicines,
    COALESCE('Alergias: '|| COALESCE ((
    select string_agg(s.pt,', ')
    from document_allergy_intolerance dai
    join allergy_intolerance ai on (ai.id=dai.allergy_intolerance_id)
    join snomed s on (s.id=ai.snomed_id)
    where dai.document_id = d.id
    and d.type_id in (1, 2, 3, 4, 9, 11, 16)
    group by dai.document_id),
    (SELECT CASE WHEN drc.is_referred IS FALSE THEN 'No refiere' ELSE NULL END
    FROM document_referable_concept drc
    WHERE drc.document_id = d.id
    AND drc.referable_concept_id = 1)),
    '') as allergies,
    COALESCE('Vacunas: ' || (select string_agg(case when i.administration_date is not null then s.pt || ' (' || to_char(i.administration_date, 'dd-mm-yyyy') || ')' else s.pt END,', ')
    from document_inmunization di
    join inmunization i on (i.id = di.inmunization_id)
    left outer join snomed s on (s.id = i.snomed_id)
    where di.document_id = d.id and d.type_id in (1, 2, 3, 4, 8, 12, 13) group by di.document_id ), '') as vaccines,
    COALESCE('Grupo y factor sanguíneo: '|| (select string_agg(replace(replace(s.pt, 'hallazgo relacionado con el grupo sanguíneo', ''),'(hallazgo)','') || ol.value, ', ')
    from document_lab dl
    join observation_lab ol on (ol.id=dl.observation_lab_id)
    join snomed s on (s.id=ol.snomed_id)
    where dl.document_id = d.id and d.type_id in (1, 2, 4, 10, 13, 16) and ol.status_id = '261782000' group by dl.document_id),'') as blood_type,
    COALESCE('Datos antropométricos: '|| (select string_agg(replace(s.pt, '(entidad observable)', '') || ': ' || ovs.value, ', ')
    from document_vital_sign dvs
    join observation_vital_sign ovs on (ovs.id=dvs.observation_vital_sign_id)
    join snomed s on (s.id=ovs.snomed_id)
    where dvs.document_id = d.id and s.sctid in ('50373000', '27113001', '363812007') and ovs.status_id = '261782000' group by dvs.document_id),'') as anthropometric_data,
    COALESCE('Signos vitales y factores de riesgo: '|| (select string_agg(replace(replace(s.pt, '(entidad observable)', ''), '(sustancia)', '') || ': ' || ovs.value, ', ')
    from document_vital_sign dvs
    join observation_vital_sign ovs on (ovs.id=dvs.observation_vital_sign_id)
    join snomed s on (s.id=ovs.snomed_id)
    where dvs.document_id = d.id and d.type_id in (1, 2, 4, 10, 13, 16) and s.sctid not in ('50373000', '27113001', '363812007') and ovs.status_id = '261782000' group by dvs.document_id),'') ||
    COALESCE('Signos vitales y factores de riesgo: '|| (select string_agg(replace(replace(s.pt, '(entidad observable)', ''), '(sustancia)', '') || ': ' || ovs.value, ', ')
    from triage_vital_signs tvs
    join observation_vital_sign ovs on (ovs.id = tvs.observation_vital_sign_id)
    join snomed s on (s.id = ovs.snomed_id)
    join triage t on (t.id = tvs.triage_id)
    join document_triage dt on (t.id = dt.triage_id)
    where dt.document_id = d.id and d.type_id = 15 and ovs.status_id = '261782000'), '') as risk_factors,
    COALESCE('Otras circunstancias que prolongan la epicrisis: ' || (select string_agg(s.pt, ', ')
    from document_health_condition dhc
    join health_condition hc on (hc.id = dhc.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where dhc.document_id = d.id and d.type_id = 3 and hc.problem_id = '00000001' group by dhc.document_id), '') as epicrisis_other_circumstances,
    COALESCE('Causa externa de traumatismo, envenenamiento y otros efectos adversos: ' || (select string_agg(
    (case when ec.external_cause_type_id is not null then '\nProducido por: ' || ect.description || '. ' else '' end) ||
    (case when ec.event_location is not null then '\nLugar donde ocurrió: ' || (
    (case when ec.event_location = 1 then 'Domicilio particular. ' else '' end) ||
    (case when ec.event_location = 2 then 'Vía pública. ' else '' end) ||
    (case when ec.event_location = 3 then 'Lugar de trabajo. ' else '' end) ||
    (case when ec.event_location = 4 then 'Otro. ' else '' end)) else '' end ) ||
    (case when ec.snomed_id is not null then '\nCómo se produjo: ' || s.pt else '' end), ', ')
    from document_external_cause dec2
    join external_cause ec on (ec.id = dec2.external_cause_id)
    left outer join external_cause_type ect on (ect.id = ec.external_cause_type_id)
    left outer join snomed s on (s.id = ec.snomed_id)
    where dec2.document_id = d.id and d.type_id = 3 and (ec.snomed_id is not null or ec.external_cause_type_id is not null or ec.event_location is not null)), '') as epicrisis_external_cause,
    COALESCE('Datos del evento obstétrico: ' || (select string_agg(
    (case when oe.previous_pregnancies is not null then '\nGestas anteriores: ' || oe.previous_pregnancies else '' end) ||
    (case when oe.current_pregnancy_end_date is not null then '\nFecha de término de gesta actual: ' || to_char(oe.current_pregnancy_end_date, 'dd-mm-yyyy') else '' end) ||
    (case when oe.gestational_age is not null then '\nEdad gestacional de gesta actual: ' || oe.gestational_age else '' end) ||
    (case when oe.pregnancy_termination_type is not null then '\nTerminación de gesta actual: ' ||
    (case when oe.pregnancy_termination_type = 1 then 'Vaginal' else '' end) ||
    (case when oe.pregnancy_termination_type = 2 then 'Cesárea' else '' end) ||
    (case when oe.pregnancy_termination_type = 3 then 'Sin definir' else '' end) else '' end), ', ')
    from document_obstetric_event doe
    join obstetric_event oe on (oe.id = doe.obstetric_event_id)
    where doe.document_id = d.id and d.type_id = 3
    and (oe.previous_pregnancies is not null or oe.current_pregnancy_end_date is not null or oe.gestational_age is not null or oe.pregnancy_termination_type is not null) ), '') as epicrisis_obstetric_event,
    COALESCE('Pediatría: ' || (select string_agg(
    (case when bt.description is not null then 'temperatura: ' || bt.description || ', ' else '' end) ||
    (case when td.crying_excessive is true then 'llanto sin consuelo, ' else '' end) ||
    (case when mh.description is not null then 'tono muscular: ' || mh.description || ', ' else '' end) ||
    (case when rr.description is not null then 'respiración con tiraje: ' || rr.description || ', ' else '' end) ||
    (case when td.stridor is true then 'respiración con estridor, ' else '' end) ||
    (case when p.description is not null then 'perfusión: ' || p.description else '' end), ', ')
    from triage_details td
    left outer join body_temperature bt on (td.body_temperature_id  = bt.id)
    left outer join muscle_hypertonia mh on (mh.id = td.muscle_hypertonia_id)
    left outer join respiratory_retraction rr on (rr.id = td.respiratory_retraction_id)
    left outer join perfusion p on (p.id = td.perfusion_id)
    join triage t on (t.id = td.triage_id)
    join document_triage dt on (t.id = dt.triage_id)
    where dt.document_id = d.id and d.type_id = 15), '') as pediatric_risk_factors,
    COALESCE('\nSolicitudes de referencia:\n' ||
    (with temporal_references as
    (select distinct re.id as reference_id, s.id as snomed_id
    from reference re
    join reference_health_condition rhc on (re.id = rhc.reference_id )
    join health_condition hc on (hc.id = rhc.health_condition_id)
    join snomed s on (s.id = hc.snomed_id)
    where re.encounter_id = d.source_id and d.type_id in (4, 9) and d.source_type_id = re.source_type_id and re.deleted IS NULL OR re.deleted = FALSE)
    select string_agg('\nProblemas asociados: ' ||
    (select string_agg(s2.pt, ',')
    from reference re2
    join reference_health_condition rhc2 on (re2.id = rhc2.reference_id)
    join health_condition hc2 on (hc2.id = rhc2.health_condition_id)
    join snomed s2 on (s2.id = hc2.snomed_id)
    where re2.id = re.id and s2.id in (select snomed_id from temporal_references where re2.id = reference_id)) ||
    (case when re.care_line_id is not null then '\nLinea de cuidado: ' || cl.description else '' end ) || '\nEspecialidad: ' || clinical_specialties || '\nInstitución destino: ' || i.name ||
    (case when re.reference_note_id is not null then '\nObservaciones: ' || rn.description else '' end),'\n')
    from reference re
    join (
    select re2.id, STRING_AGG(cs."name" , ', ') as clinical_specialties
    from reference re2
    join reference_clinical_specialty rcs on (rcs.reference_id = re2.id)
    join clinical_specialty cs on (cs.id = rcs.clinical_specialty_id)
    group by re2.id
    ) as reference_clinical_specialties on (reference_clinical_specialties.id = re.id)
    join institution i on (i.id = re.destination_institution_id)
    left outer join care_line cl on (cl.id = re.care_line_id)
    left outer join reference_note rn on (rn.id = re.reference_note_id)
    where re.encounter_id = d.source_id and d.type_id in (4, 9) and re.source_type_id = d.source_type_id group by d.id), '') as outpatient_references,
    COALESCE('Categoria: '|| (select string_agg(src.description,'' )
    from service_request sr
    left outer join service_request_category src  on (sr.category_id=src.id)
    left outer join service_request_status srs on (sr.status_id=srs.id)
    where sr.id = d.source_id and d.type_id = 6),'') as service_request_category,
    COALESCE(
    (with temporal_orders as
    (select distinct d.source_id as source_id, dr.snomed_id as snomed_id, dr.health_condition_id as health_condition_id
    from document_diagnostic_report ddr
    join diagnostic_report dr on (dr.id = ddr.diagnostic_report_id)
    join health_condition hc on (hc.id=dr.health_condition_id)
    where ddr.document_id = d.id and d.type_id = 6 and dr.status_id <> '89925002')
    select string_agg('Estudio: ' || s.pt || (case when dr.note_id is not null and dr.status_id = '261782000' then '.\nResultado: ' || n.description || ' (' || to_char(dr.effective_time, 'dd-mm-yyyy') || ')' else '. (Pendiente)' end),'\n')
    from document_diagnostic_report ddr
    join diagnostic_report dr on (dr.id=ddr.diagnostic_report_id)
    join health_condition hc on (hc.id=dr.health_condition_id)
    join snomed s on (s.id=dr.snomed_id)
    left outer join note n on (dr.note_id = n.id)
    where ddr.document_id = d.id
    and d.type_id = 6
    and dr.status_id  <> '89925002'
    and exists (select 1 from temporal_orders t where d.source_id = t.source_id and dr.snomed_id = t.snomed_id and dr.health_condition_id = t.health_condition_id)
    and not exists
    (select 1
    from document_diagnostic_report ddr2
    join diagnostic_report dr2 on (dr2.id = ddr2.diagnostic_report_id)
    where ddr2.document_id = ddr.document_id
    and dr2.id <> dr.id
    and dr2.patient_id = dr.patient_id
    and dr2.snomed_id = dr.snomed_id
    and dr2.health_condition_id = dr2.health_condition_id
    and dr2.status_id in ('261782000', '89925002')) group by ddr.document_id), '') as service_request_studies,
    COALESCE('Motivos de consulta: '|| (select string_agg(r.description,', ' )
    from outpatient_consultation oc
    left outer join outpatient_consultation_reasons ocr on (oc.id=ocr.outpatient_consultation_id)
    join reasons r on (r.id=ocr.reason_id)
    where oc.document_id = d.id and d.type_id = 4 group by oc.document_id),'') ||
    COALESCE('Motivos de consulta: ' || (select string_agg(r.description ,', ')
    from emergency_care_evolution_note ecen
    join emergency_care_evolution_note_reason ecenr on (ecenr.emergency_care_evolution_note_id = ecen.id)
    join reasons r on (r.id  = ecenr.reason_id)
    where ecen.document_id = d.id and d.type_id = 16 group by ecen.document_id),'') as consultation_reasons,
    COALESCE('Procedimientos sobre tejidos dentales: '|| (select string_agg(distinct('\n' || s.pt || ' en ' || s1.pt),'' )
    from document_odontology_procedure dop
    join odontology_procedure op on (op.id=dop.odontology_procedure_id)
    join snomed s on (s.id=op.snomed_id)
    left join snomed s1 on (op.tooth_id = s1.id)
    where dop.document_id = d.id and d.type_id = 9 group by dop.document_id),'') as odontology_procedure,
    COALESCE('Hallazgos sobre tejidos dentales: '|| (select string_agg(distinct ('\n' || s.pt || ' en ' || s1.pt),'' )
    from document_odontology_diagnostic dod
    join odontology_diagnostic od on (od.id=dod.odontology_diagnostic_id)
    join snomed s on (s.id=od.snomed_id)
    left outer join snomed s1 on (s1.id=od.tooth_id)
    where dod.document_id = d.id and d.type_id = 9 group by dod.document_id),'') as odontology_diagnostic,
    COALESCE('Cantidad de piezas presentes: ' || (select string_agg(
    (case when oci.permanent_teeth_present is not null then 'permanentes: ' || oci.permanent_teeth_present ||
    (case when oci.temporary_teeth_present is not null then ', ' else '' end) else '' end) ||
    (case when oci.temporary_teeth_present is not null then 'temporales: ' || oci.temporary_teeth_present else '' end), ', ')
    from odontology_consultation_indices oci
    where d.type_id = 9 and d.source_id = oci.odontology_consultation_id), '') as odontology_pieces,
    COALESCE('Dieta: '|| (select string_agg(die.description || '\nFecha indicación: ' || to_char(i.indication_date,'dd-mm-yyyy'), ', ' )
    from document_indication di
    join indication i on (i.id=di.indication_id )
    join diet die on (die.id=i.id)
    where d.id = di.document_id and d.type_id = 12 and i.type_id = 2 group by di.document_id), '') ||
    COALESCE('Otra indicación: '|| (select string_agg
    (case when oi.other_type is not null then oit.description || ': ' || oi.other_type else oit.description end || '\nFrecuencia: ' ||
    (case when dos.period_unit is null then 'Sin especificar' else '' end) ||
    (case when dos.period_unit = 'd' then 'Única vez' else '' end) ||
    (case when dos.period_unit = 'h' then 'Cada ' || dos.frequency || ' hs' else '' end) ||
    (case when dos.period_unit = 'e' then 'Ante evento: ' || dos."event" else '' end) ||
    (case when oi.description is not null then '\nNotas: ' || oi.description else '' end) || '\nFecha indicación: ' || to_char(i.indication_date, 'dd-mm-yyyy'),', ' )
    from document_indication di
    join indication i on (i.id=di.indication_id )
    join other_indication oi on (oi.id=i.id)
    join other_indication_type oit on (oi.other_indication_type_id=oit.id)
    left join dosage dos on (dos.id = oi.dosage_id)
    where d.id = di.document_id and d.type_id = 12 and i.type_id = 4 group by di.document_id), '') ||
    COALESCE('Fármaco: '|| (select string_agg(s1.pt ||
    (case when q is not null then '\nVol/bolsa: ' || q.value || ' ' || q.unit else '' end) ||
    (case when s2.pt is not null then '\nDiluyente: ' || s2.pt else '' end) || '\nAdministración: ' ||
    (case when dos.period_unit = 'd' then 'Única vez' else '' end) ||
    (case when dos.period_unit = 'h' then 'Cada ' || dos.frequency || ' hs' else '' end) ||
    (case when dos.period_unit = 'e' then 'Ante evento: ' || dos."event" else '' end) || '\nRelación con las comidas: ' ||
    (case when ph.food_relation_id = 0 then 'No' else '' end) ||
    (case when ph.food_relation_id = 1 then 'Lejos' else '' end) ||
    (case when ph.food_relation_id = 2 then 'Ayuno' else '' end) ||
    '\nDiagnóstico asociado: ' || s.pt || '\nFecha indicación: ' || to_char(i.indication_date, 'dd-mm-yyyy'),'\n' )
    from document_indication di
    join indication i on (i.id=di.indication_id )
    join pharmaco ph on (ph.id=i.id)
    join health_condition hc on (hc.id=ph.health_condition_id)
    join snomed s on (s.id=hc.snomed_id)
    join snomed s1 on (s1.id=ph.snomed_id)
    left outer join other_pharmaco oph on (oph.indication_id = i.id)
    left outer join snomed s2 on (s2.id = oph.snomed_id)
    join dosage dos on (dos.id=ph.dosage_id)
    left outer join quantity q on (q.id=dos.dose_quantity_id)
    where d.id = di.document_id and d.type_id = 12 and i.type_id = 1 group by di.document_id),'') ||
    COALESCE('Plan parenteral: '||(select string_agg(s.pt ||
    (case when q is not null then '\nVol/bolsa: ' || q.value || ' ' || q.unit else '' end) ||
    (case when f.duration is not null then '\nDuración: ' || to_char(f.duration, 'HH24:MI') || ' hs' else '' end) ||
    '\nFlujo: ' || f.flow_ml_hour || ' ml/h | ' || f.flow_drops_hour || ' hp. Gotas' ||
    (case when f.daily_volume is not null then '\nVol/Día: ' || f.daily_volume || 'ml' else '' end) ||
    (case when s1.pt is not null then '\nFármaco: ' || s1.pt else '' end) || '\nFecha indicación: ' || to_char(i.indication_date, 'dd-mm-yyyy') ,', ' )
    from document_indication di
    join indication i on (i.id=di.indication_id )
    join parenteral_plan pp on (pp.id=i.id)
    join snomed s on (pp.snomed_id=s.id)
    join dosage dos on (pp.dosage_id=dos.id)
    join frequency f on (f.id = pp.frequency_id)
    left outer join quantity q on (q.id=dos.dose_quantity_id )
    left outer join other_pharmaco oph on (oph.indication_id=pp.id)
    left outer join snomed s1 on (s1.id=oph.snomed_id)
    where d.id = di.document_id and d.type_id = 12 and i.type_id = 3 group by di.document_id),'') ||
    COALESCE ('\nEstado: ' || (select string_agg(
    (case when i.status_id = 1 then 'Indicada' else '' end) ||
    (case when i.status_id = 2 then 'Suspendida' else '' end) ||
    (case when i.status_id = 3 then 'En progreso' else '' end) ||
    (case when i.status_id = 4 then 'Completada' else '' end) ||
    (case when i.status_id = 5 then 'Rechazada' else '' end), ', ')
    from document_indication di
    join indication i on (i.id = di.indication_id)
    where di.document_id = d.id and d.type_id = 12 group by di.document_id),'') as indication,
    COALESCE('Detalles de referencia solicitada: ' || (select string_agg(
    (case when re.care_line_id is not null then '\nLinea de cuidado: ' || cl.description else '' end) || '\nEspecialidad: ' || clinical_specialties ||
    (case when re.reference_note_id is not null then '\nObservaciones: ' || rn.description else '' end), ', ')
    from counter_reference cr
    join reference re on (re.id = cr.reference_id)
    join (
    select re2.id, STRING_AGG(cs."name" , ', ') as clinical_specialties
    from reference re2
    join reference_clinical_specialty rcs on (rcs.reference_id = re2.id)
    join clinical_specialty cs on (cs.id = rcs.clinical_specialty_id)
    group by re2.id
    ) as reference_clinical_specialties on (reference_clinical_specialties.id = re.id)
    left outer join care_line cl on (cl.id = re.care_line_id)
    left outer join reference_note rn on (rn.id = re.reference_note_id)
    where cr.id = d.source_id and d.type_id = 11), '') as reference_counter_reference,
    COALESCE('Tipo de cierre: ' || (select string_agg(
    (case when cr.closure_type_id = 1 then 'Continúa en observación' else '' end) ||
    (case when cr.closure_type_id = 2 then 'Inicia tratamiento en centro de referencia' else '' end) ||
    (case when cr.closure_type_id = 3 then 'Requiere estudios complementarios' else '' end) ||
    (case when cr.closure_type_id = 4 then 'Contrarreferencia' else '' end), ', ')
    from counter_reference cr
    where cr.id = d.source_id and d.type_id = 11), '') ||
    COALESCE((select string_agg('\nDescripción de cierre: ' || n.description, ', ' )
    from note n
    where n.id = d.evolution_note_id and d.type_id = 11), '') as counter_reference_closure,
    COALESCE((select string_agg(tc."name", ', ')
    from document_triage dt
    join triage t on (t.id = dt.triage_id)
    join triage_category tc on (tc.id = t.triage_category_id)
    where dt.document_id = d.id and d.type_id = 15 group by d.id),'') as triage_level,
    COALESCE('\nEvolución: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.evolution_note_id and d.type_id = 3), '') ||
    COALESCE('\nEnfermedad actual: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.current_illness_note_id and d.type_id in (1, 2)), '') ||
    COALESCE('\nIndicaciones al alta: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.indications_note_id and d.type_id = 3), '') ||
    COALESCE('\nResumen de estudios y procedimientos realizados: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.studies_summary_note_id and d.type_id = 3), '') ||
    COALESCE('\nExamen físico: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.physical_exam_note_id and d.type_id in (1, 2, 3)), '') ||
    COALESCE('\nResumen de estudios y procedimientos realizados: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.studies_summary_note_id and d.type_id in (1, 2)), '') ||
    COALESCE('\nEvolución: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.evolution_note_id and d.type_id IN (1, 2)), '') ||
    COALESCE('\nImpresión clínica y plan: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.clinical_impression_note_id and d.type_id in (1, 2)), '') ||
    COALESCE('\nOtras observaciones: '|| (select string_agg(n.description, ',' )
    from note n
    where n.id = d.other_note_id and d.type_id in (1, 2, 3)), '') ||
    COALESCE('Evolución: ' || (select string_agg(n.description, ', ')
    from note n
    where n.id = d.other_note_id and d.type_id in (4, 15, 16)), '') ||
    COALESCE('Evolución: '|| (select string_agg(n.description, ', ' )
    from note n
    where n.id = d.evolution_note_id and d.type_id in (9, 10, 13)), '') as notes
    from
    document as d
    join document_type dt ON (d.type_id=dt.id)
    join institution i on (i.id=d.institution_id)
    left outer join clinical_specialty cs on (d.clinical_specialty_id=cs.id)
    where d.deleted=false and d.status_id = '445665009'
);
END
$func$;
COMMIT;
