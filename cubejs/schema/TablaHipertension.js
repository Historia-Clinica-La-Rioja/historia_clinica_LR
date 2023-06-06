cube(`TablaHipertension`, {
    sql: `WITH rc AS (
        SELECT srg.snomed_id  
        FROM snomed_related_group srg 
        WHERE srg.group_id = 37),
        dp AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 2
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
        sp AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 1
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
        cr AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 1482
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
        lp AS (
        SELECT p.patient_id, s.pt, p.performed_date
        FROM procedures p
        JOIN Snomed s ON (s.id = p.snomed_id)
        WHERE p.id = (SELECT p2.id
                        FROM procedures p2 
                        WHERE p2.patient_id = p.patient_id  
                        ORDER BY p2.performed_date desc 
                        LIMIT 1)),
        la AS (
        SELECT hc.patient_id, hc.snomed_id, hc.created_on 
        FROM health_condition hc
        WHERE hc.id = (SELECT hc2.id
                        FROM health_condition hc2 
                        WHERE hc2.patient_id = hc.patient_id 
                        AND hc2.snomed_id in (select snomed_id from rc)
                        AND hc2.snomed_id = hc.snomed_id
                        ORDER BY hc2.created_on DESC
                        LIMIT 1))
        SELECT DISTINCT CASE WHEN p3.name_self_determination IS NULL THEN CONCAT(p2.first_name, ' ', p2.middle_names) WHEN p3.name_self_determination IS NOT NULL THEN p3.name_self_determination END AS name, 
        CONCAT(p2.last_name, ' ', p2.other_last_names) AS last_name, it.description || ' ' || p2.identification_number AS identification_number, s.pt AS problem, TO_CHAR(hc.start_date, 'DD/MM/YYYY') AS problem_start_date, 
        hcs.display AS severity, a.street || ' ' || a."number" AS address, c.description AS city_name, cr.value || '% (' || TO_CHAR(cr.creation_date, 'DD/MM/YYYY') || ')' AS cardiovascular_risk_value,
        dp.value || ' (' || to_char(dp.creation_date, 'DD/MM/YYYY') || ')' AS diastolic_pressure_value, sp.value || ' (' || to_char(sp.creation_date, 'DD/MM/YYYY') || ')' AS systolic_pressure_value,
        lp.pt || ' (' || TO_CHAR(lp.performed_date, 'DD/MM/YYYY') || ')' AS last_procedure, TO_CHAR(la.created_on, 'DD/MM/YYYY') AS last_attention_date, d.institution_id
        FROM document d 
        JOIN document_health_condition dhc ON (dhc.document_id = d.id)
        JOIN health_condition hc ON (hc.id = dhc.health_condition_id)
        JOIN snomed s ON (s.id = hc.snomed_id) 
        LEFT JOIN health_condition_severity hcs ON (hcs.code = hc.severity)
        JOIN patient p ON (p.id = hc.patient_id)
        JOIN person p2 ON (p2.id = p.person_id)
        JOIN person_extended p3 ON (p3.person_id = p2.id)
        JOIN identification_type it ON (p2.identification_type_id = it.id)
        LEFT JOIN person_extended pe ON (pe.person_id = p2.id)
        LEFT JOIN address a ON (a.id = pe.address_id)
        LEFT JOIN city c ON (c.id = a.city_id)
        LEFT JOIN cr ON (cr.patient_id = p.id)
        LEFT JOIN dp ON (dp.patient_id = p.id)
        LEFT JOIN sp ON (sp.patient_id = p.id)
        LEFT JOIN lp ON (lp.patient_id = p.id)
        JOIN la ON (la.patient_id = hc.patient_id AND la.snomed_id = hc.snomed_id)
        WHERE hc.id = (SELECT hc2.id  
                    FROM document d2
                    JOIN document_health_condition dhc2 ON (dhc2.document_id = d2.id)
                    JOIN health_condition hc2 ON (hc2.id = dhc2.health_condition_id) 
                    WHERE hc.snomed_id IN (
                        SELECT rc.snomed_id  
                        FROM rc)
                    AND hc2.created_on <= CURRENT_DATE - INTERVAL '120' DAY
                    AND hc2.problem_id IN ('55607006', '-55607006')
                    AND d2.status_id IN ('445665009', '445667001') 
                    AND d2.type_id IN (4, 9) 
                    AND hc2.start_date IS NOT NULL
                    AND hc2.patient_id = hc.patient_id
                    AND hc2.snomed_id = hc.snomed_id
                    ${SECURITY_CONTEXT.roles.unsafeValue()?.filter(role => role.id === 16 || role.id === 8).length === 0 ? '' +  `
                    AND d.created_by = ${SECURITY_CONTEXT.userId.unsafeValue()}` : ''}
                    ORDER BY hc2.start_date
                    LIMIT 1)`,
    measures: {
        pacientes_hipertensos_sin_atencion: {
            sql: `*`,
            type: `count`,
            title: `Pacientes Hipertensos sin atención por mas de 120 dias`
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
        valor_presion_diastolica: {
            sql: `diastolic_pressure_value`,
            type: `string`,
            title: `Última Presión Diastólica`
        },
        valor_presion_sistolica: {
            sql: `systolic_pressure_value`,
            type: `string`,
            title: `Última Presión Sistólica`
        },
        valor_riesgo_cardiovascular: {
            sql: `cardiovascular_risk_value`,
            type: `string`,
            title: `Último Riesgo Cardiovascular`
        },
        ultimo_procedimiento: {
            sql: `last_procedure`,
            type: `string`,
            title: `Último Procedimiento`
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