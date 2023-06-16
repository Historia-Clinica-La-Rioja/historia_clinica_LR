cube(`TablaDiabetes`, {
    sql: `WITH rc AS (
        SELECT srg.snomed_id  
        FROM snomed_group sg
        JOIN snomed_related_group srg ON (srg.group_id = sg.id)
        WHERE sg.description = 'DIABETES'),
        cd AS (
        SELECT DISTINCT hc.patient_id, 1 AS result
        FROM health_condition hc
        JOIN snomed s ON (s.id = hc.snomed_id)
        JOIN snomed_related_group srg ON (srg.snomed_id = s.id)
        JOIN snomed_group sg ON (sg.id = srg.group_id)
        WHERE sg.description = 'CARDIOVASCULAR_DISORDER'),
        gh AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 1481
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
        lof AS (
        SELECT p.patient_id, p.performed_date
        FROM procedures p
        WHERE p.id = (SELECT p2.id
                        FROM procedures p2 
                        WHERE p2.patient_id = p.patient_id 
                        AND p2.snomed_id = 174915 
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
        dp1 AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 2
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
        sp1 AS (
        SELECT ovs.patient_id, ovs.value, date(ovs.effective_time) AS creation_date
        FROM observation_vital_sign ovs 
        WHERE ovs.id = (SELECT ovs2.id 
                            FROM observation_vital_sign ovs2 
                            WHERE ovs2.patient_id = ovs.patient_id 
                            AND ovs2.snomed_id = 1
                            ORDER BY ovs2.effective_time DESC
                            LIMIT 1)),
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
        ldfe AS (
        SELECT p.patient_id, p.performed_date
        FROM procedures p
        WHERE p.id = (SELECT p2.id
                        FROM procedures p2 
                        WHERE p2.patient_id = p.patient_id 
                        AND p2.snomed_id = 58833 
                        ORDER BY p2.performed_date desc 
                        LIMIT 1)),
        leasmod AS (
        SELECT p.patient_id, p.performed_date
        FROM procedures p
        WHERE p.id = (SELECT p2.id
                        FROM procedures p2 
                        WHERE p2.patient_id = p.patient_id 
                        AND p2.snomed_id = 36860 
                        ORDER BY p2.performed_date desc 
                        LIMIT 1)),
        lf AS (
        SELECT dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr  
        WHERE dr.id = (SELECT dr2.id 
                            FROM diagnostic_report dr2 
                            join snomed s on (s.id = dr2.snomed_id)
                            WHERE dr2.patient_id = dr.patient_id 
                            and s.sctid = '401531000221109'
                            and dr2.status_id = '261782000'
                            ORDER BY dr2.updated_on DESC
                            LIMIT 1)),
        lac AS (
        SELECT dr.patient_id, date(dr.created_on) AS result_date
        FROM diagnostic_report dr  
        WHERE dr.id = (SELECT dr2.id 
                            FROM diagnostic_report dr2 
                            join snomed s on (s.id = dr2.snomed_id)
                            WHERE dr2.patient_id = dr.patient_id 
                            and s.sctid = '250745003'
                            and dr2.status_id = '261782000'
                            ORDER BY dr2.updated_on DESC
                            LIMIT 1))             
        SELECT DISTINCT CASE WHEN p3.name_self_determination IS NULL THEN CONCAT(p2.first_name, ' ', p2.middle_names) WHEN p3.name_self_determination IS NOT NULL THEN p3.name_self_determination END AS name, 
        CONCAT(p2.last_name, ' ', p2.other_last_names) AS last_name, it.description || ' ' || p2.identification_number AS identification_number, s.pt AS problem, TO_CHAR(hc.start_date, 'DD/MM/YYYY') AS problem_start_date, 
        a.street || ' ' || a."number" AS address, c.description AS city_name, gh.value || '% (' || TO_CHAR(gh.creation_date, 'DD/MM/YYYY') || ')' AS glycosilated_hemoglobin_value,
        TO_CHAR(lof.performed_date, 'DD/MM/YYYY') AS last_ocular_fondus_date, TO_CHAR(la.created_on, 'DD/MM/YYYY') AS last_attention_date, d.institution_id,
        TO_CHAR(p2.birth_date, 'DD/MM/YYYY') AS birth_date, cr.value || '% (' || TO_CHAR(cr.creation_date, 'DD/MM/YYYY') || ')' AS cardiovascular_risk_value,
        dp1.value || ' (' || to_char(dp1.creation_date, 'DD/MM/YYYY') || ')' AS last_diastolic_pressure_value, sp1.value || ' (' || to_char(sp1.creation_date, 'DD/MM/YYYY') || ')' AS last_systolic_pressure_value,
        dp2.value || ' (' || to_char(dp2.creation_date, 'DD/MM/YYYY') || ')' AS penultimate_diastolic_pressure_value, sp2.value || ' (' || to_char(sp2.creation_date, 'DD/MM/YYYY') || ')' AS penultimate_systolic_pressure_value,
        TO_CHAR(ldfe.performed_date, 'DD/MM/YYYY') AS last_diabetic_foot_examination_date, TO_CHAR(leasmod.performed_date, 'DD/MM/YYYY') AS last_education_about_self_management_of_diabetes_date,
        TO_CHAR(lf.result_date, 'DD/MM/YYYY') AS last_filtration_date, TO_CHAR(lac.result_date, 'DD/MM/YYYY') AS last_albumin_creatinine_date,
        CASE WHEN cd.result IS NULL THEN 'No' WHEN cd.result IS NOT NULL THEN 'Si' END AS cardiovascular_disorder_antecedent
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
        LEFT JOIN gh ON (gh.patient_id = p.id)
        LEFT JOIN lof ON (lof.patient_id = p.id)
        LEFT JOIN cr ON (cr.patient_id = p.id)
        LEFT JOIN dp1 ON (dp1.patient_id = p.id)
        LEFT JOIN sp1 ON (sp1.patient_id = p.id)
        LEFT JOIN dp2 ON (dp2.patient_id = p.id)
        LEFT JOIN sp2 ON (sp2.patient_id = p.id)
        LEFT JOIN ldfe ON (ldfe.patient_id = p.id)
        LEFT JOIN leasmod ON (leasmod.patient_id = p.id)
        LEFT JOIN lf ON (lf.patient_id = p.id)
        LEFT JOIN lac ON (lac.patient_id = p.id)
        LEFT JOIN cd ON (cd.patient_id = p.id)
        JOIN la ON (la.patient_id = hc.patient_id AND la.snomed_id = hc.snomed_id)
        WHERE hc.id = (SELECT hc2.id  
                    FROM document d2
                    JOIN document_health_condition dhc2 ON (dhc2.document_id = d2.id)
                    JOIN health_condition hc2 ON (hc2.id = dhc2.health_condition_id) 
                    WHERE hc.snomed_id IN (
                        SELECT rc.snomed_id  
                        FROM rc)
                    AND hc2.problem_id IN ('55607006', '-55607006')
                    AND d2.status_id IN ('445665009', '445667001') 
                    AND d2.type_id IN (4, 9) 
                    AND hc2.start_date IS NOT NULL
                    AND hc2.patient_id = hc.patient_id
                    AND hc2.snomed_id = hc.snomed_id
                    ORDER BY hc2.start_date
                    LIMIT 1)`,
    measures: {
        pacientes_diabeticos_sin_atencion: {
            sql: `*`,
            type: `count`,
            title: `Pacientes Diabeticos`
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
        valor_hemoglobina_glicosada: {
            sql: `glycosilated_hemoglobin_value`,
            type: `string`,
            title: `Última Hemoglobina Glicosada`
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
        fecha_fondo_de_ojo: {
            sql: `last_ocular_fondus_date`,
            type: `string`,
            title: `Fecha fondo de ojo`
        },
        fecha_examen_pie_diabetico: {
            sql: `last_diabetic_foot_examination_date`,
            type: `string`,
            title: `Fecha examen de pié diabético `
        },
        fecha_educacion_automanejo_diabetes: {
            sql: `last_education_about_self_management_of_diabetes_date`,
            type: `string`,
            title: `Fecha educación sobre automanejo de diabetes`
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
        antecedente_enfermedad_cardiovascular: {
            sql: `cardiovascular_disorder_antecedent`,
            type: `string`,
            title: `Antecedente enfermedad cardiovascular`
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