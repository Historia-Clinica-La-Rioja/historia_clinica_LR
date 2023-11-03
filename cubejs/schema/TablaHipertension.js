cube(`TablaHipertension`, {
    sql: `WITH rc AS (
        SELECT srg.snomed_id  
        FROM snomed_group sg
        JOIN snomed_related_group srg ON (srg.group_id = sg.id)
        WHERE sg.description = 'HYPERTENSION'),
        dp1 AS (
        SELECT DISTINCT ON (ovs.patient_id) ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.snomed_id = 2
        ORDER BY ovs.patient_id, ovs.effective_time DESC),
        sp1 AS (
        SELECT DISTINCT ON (ovs.patient_id) ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.snomed_id = 1
        ORDER BY ovs.patient_id, ovs.effective_time DESC),
        dp2 AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 2
                            ORDER BY ovs2.effective_time DESC
                            OFFSET 1
                            LIMIT 1)),
        sp2 AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 1
                            ORDER BY ovs2.effective_time DESC
                            OFFSET 1
                            LIMIT 1)),
        cr AS (
        SELECT DISTINCT ON (ovs.patient_id) ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.snomed_id = 1482
        ORDER BY ovs.patient_id, ovs.effective_time DESC),
        la AS (
        SELECT DISTINCT ON (hc.patient_id, hc.snomed_id) hc.patient_id, hc.snomed_id, hc.created_on 
        FROM health_condition hc
        WHERE hc.snomed_id IN (SELECT snomed_id FROM rc)
        ORDER BY hc.patient_id, hc.snomed_id, hc.created_on DESC),
        lf AS (
        SELECT DISTINCT ON (dr.patient_id) dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr  
        JOIN snomed s ON (s.id = dr.snomed_id)
        WHERE s.sctid = '401531000221109'
        AND dr.status_id = '261782000'
        ORDER BY dr.patient_id, dr.updated_on DESC),
        lac AS (
        SELECT DISTINCT ON (dr.patient_id) dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr
        JOIN snomed s ON (s.id = dr.snomed_id)  
        WHERE s.sctid = '250745003'
        and dr.status_id = '261782000'
        ORDER BY dr.patient_id, dr.updated_on DESC),
        lc AS (
        SELECT DISTINCT ON (dr.patient_id) dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr  
        JOIN snomed s ON (s.id = dr.snomed_id)
        WHERE s.sctid = '70901006'
        and dr.status_id = '261782000'
        ORDER BY dr.patient_id, dr.updated_on DESC),
        lep as (
        SELECT DISTINCT ON (dr.patient_id) dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr 
        JOIN snomed s ON (s.id = dr.snomed_id)
        JOIN snomed_related_group srg ON (srg.snomed_id = s.id)
        JOIN snomed_group sg ON (sg.id = srg.group_id)
        WHERE sg.description = 'ELECTROCARDIOGRAPHIC_PROCEDURE'
        AND dr.status_id = '261782000'
        ORDER BY dr.patient_id, dr.updated_on DESC),
        lp AS (
        SELECT DISTINCT ON (hc.patient_id) hc.patient_id, date(hc.start_date) AS result_date
        FROM health_condition hc 
        JOIN document_health_condition dhc ON (dhc.health_condition_id = hc.id)
        JOIN document d ON (d.id = dhc.document_id)
        WHERE hc.snomed_id IN (SELECT rc.snomed_id FROM rc)
        AND hc.problem_id IN ('55607006', '-55607006')
        AND d.status_id IN ('445665009', '445667001') 
        AND d.type_id IN (4, 9) 
        AND hc.start_date IS NOT NULL
        ORDER BY hc.patient_id, hc.start_date DESC)
        SELECT DISTINCT CASE WHEN p3.name_self_determination IS NULL THEN CONCAT(p2.first_name, ' ', p2.middle_names) WHEN p3.name_self_determination IS NOT NULL THEN p3.name_self_determination END AS name, 
        CONCAT(p2.last_name, ' ', p2.other_last_names) AS last_name, it.description || ' ' || p2.identification_number AS identification_number, s.pt AS problem, TO_CHAR(lp.result_date, 'DD/MM/YYYY') AS problem_start_date, 
        a.street || ' ' || a."number" AS address, c.description AS city_name, cr.value || '% (' || TO_CHAR(cr.creation_date, 'DD/MM/YYYY') || ')' AS cardiovascular_risk_value,
        dp1.value || ' (' || to_char(dp1.creation_date, 'DD/MM/YYYY') || ')' AS last_diastolic_pressure_value, sp1.value || ' (' || to_char(sp1.creation_date, 'DD/MM/YYYY') || ')' AS last_systolic_pressure_value,
        dp2.value || ' (' || to_char(dp2.creation_date, 'DD/MM/YYYY') || ')' AS penultimate_diastolic_pressure_value, sp2.value || ' (' || to_char(sp2.creation_date, 'DD/MM/YYYY') || ')' AS penultimate_systolic_pressure_value,
        TO_CHAR(la.created_on, 'DD/MM/YYYY') AS last_attention_date, d.institution_id, TO_CHAR(p2.birth_date, 'DD/MM/YYYY') AS birth_date,
        TO_CHAR(lf.result_date, 'DD/MM/YYYY') AS last_filtration_date, TO_CHAR(lac.result_date, 'DD/MM/YYYY') AS last_albumin_creatinine_date,
        TO_CHAR(lc.result_date, 'DD/MM/YYYY') AS last_creatinine_date, TO_CHAR(lep.result_date, 'DD/MM/YYYY') AS last_electrocardiographic_procedure_date
        FROM document d 
        JOIN document_health_condition dhc ON (dhc.document_id = d.id)
        JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
        JOIN snomed s ON (s.id = hc.snomed_id) 
        JOIN patient p ON (p.id = hc.patient_id)
        JOIN person p2 ON (p2.id = p.person_id)
        JOIN person_extended p3 ON (p3.person_id = p2.id)
        JOIN identification_type it ON (p2.identification_type_id = it.id)
        LEFT JOIN person_extended pe ON (pe.person_id = p2.id)
        LEFT JOIN address a ON (a.id = pe.address_id)
        LEFT JOIN city c ON (c.id = a.city_id)
        LEFT JOIN cr ON (cr.patient_id = p.id)
        LEFT JOIN dp1 ON (dp1.patient_id = p.id)
        LEFT JOIN sp1 ON (sp1.patient_id = p.id)
        LEFT JOIN dp2 ON (dp2.patient_id = p.id)
        LEFT JOIN sp2 ON (sp2.patient_id = p.id)
        LEFT JOIN lf ON (lf.patient_id = p.id)
        LEFT JOIN lac ON (lac.patient_id = p.id)
        LEFT JOIN lc ON (lc.patient_id = p.id)
        LEFT JOIN lep ON (lep.patient_id = p.id)
        JOIN la ON (la.patient_id = hc.patient_id AND la.snomed_id = hc.snomed_id)
        JOIN lp ON (lp.patient_id = p.id)`,
    measures: {
        pacientes_hipertensos_sin_atencion: {
            sql: `*`,
            type: `count`,
            title: `Pacientes Hipertensos`
        }
    },
    dimensions: {
        primer_nombre: {
            sql: `name`,
            type: `string`,
            title: `Nombre`
        },
        apellido: {
            sql: `last_name`,
            type: `string`,
            title:`Apellido`
        },
        numero_identificacion: {
            sql: `identification_number`,
            type: `string`,
            title: `Número de identificación`
        },
        fecha_nacimiento: {
            sql: `birth_date`,
            type: `string`,
            title: `Fecha de Nacimiento`
        },
        problema: {
            sql: `problem`,
            type: `string`,
            title: `Problema`
        },
        fecha_deteccion_diagnostico: {
            sql: `problem_start_date`,
            type: `string`,
            title: `Fecha detección`
        },
        ultima_fecha_atencion: {
            sql: `last_attention_date`,
            type: `string`,
            title: `Última fecha atención`
        },
        severidad: {
            sql: `severity`,
            type: `string`,
            title: `Severidad`
        },
        direccion: {
            sql: `address`,
            type: `string`,
            title: `Domicilio`
        },
        nombre_ciudad: {
            sql: `city_name`,
            type: `string`,
            title: `Ciudad`
        },
        ultimo_valor_presion_diastolica: {
            sql: `last_diastolic_pressure_value`,
            type: `string`,
            title: `Última Tensión Diastólica`
        },
        anteultimo_valor_presion_diastolica: {
            sql: `penultimate_diastolic_pressure_value`,
            type: `string`,
            title: `Anteúltima Tensión Diastólica`
        },
        ultimo_valor_presion_sistolica: {
            sql: `last_systolic_pressure_value`,
            type: `string`,
            title: `Última Tensión Sistólica`
        },
        anteultimo_valor_presion_sistolica: {
            sql: `penultimate_systolic_pressure_value`,
            type: `string`,
            title: `Anteúltima Tensión Sistólica`
        },
        valor_riesgo_cardiovascular: {
            sql: `cardiovascular_risk_value`,
            type: `string`,
            title: `Último Riesgo Cardiovascular`
        },
        fecha_filtracion_glomerular: {
            sql: `last_filtration_date`,
            type: `string`,
            title: `Fecha medición IFGe - índice de filtración glomerular estimada`
        },
        fecha_albumina_creatinina: {
            sql: `last_albumin_creatinine_date`,
            type: `string`,
            title: `Fecha índice albúmina/creatinina`
        },
        fecha_creatinina: {
            sql: `last_creatinine_date`,
            type: `string`,
            title: `Fecha medición de creatinina`
        },
        fecha_electrocardiograma: {
            sql: `last_electrocardiographic_procedure_date`,
            type: `string`,
            title: `Fecha electrocardiograma`
        },
        institucion: {
            sql: `institution_id`,
            type: `number`,
            title: `Intitución`
        }
    },
    title:` `,
    dataSource: `default`
});