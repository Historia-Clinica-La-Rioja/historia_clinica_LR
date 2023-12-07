cube(`TablaEpidemiologia`, {
    sql: `WITH lc AS (
        SELECT DISTINCT ON (hc.patient_id, hc.snomed_id) hc.id, hc.patient_id, hc.snomed_id, hc.created_on
        FROM health_condition hc
        WHERE hc.status_id = '723506003'
        AND hc.problem_id IN ('439401001', '55607006', '-55607006')
        AND hc.verification_status_id = '59156000'
        ORDER BY hc.patient_id, hc.snomed_id, hc.created_on DESC
      ),
      ia AS (
          SELECT i.id, i.sisa_code, i."name", p.description as state, d.description as department, c.description as city
          FROM institution i
          LEFT JOIN address a ON (a.id = i.address_id)
          LEFT JOIN province p ON (p.id = a.province_id)
          LEFT JOIN department d ON (d.id = a.department_id)
          LEFT JOIN city c ON (c.id = a.city_id)
      ),
      preg AS (
          SELECT hc.patient_id, hc.created_on, hc.id 
          FROM health_condition hc
          JOIN snomed s on (s.id = hc.snomed_id)
          WHERE s.sctid = '77386006'
      ),
      internment AS (
          SELECT ie.patient_id, ie.created_on, pd.administrative_discharge_date, ie.id, pd.discharge_type_id  
          FROM internment_episode ie 
          LEFT JOIN patient_discharge pd ON (pd.internment_episode_id = ie.id)
      ),
      uti AS (
          SELECT ie.patient_id, ie.created_on, pd.administrative_discharge_date, ie.id 
          FROM internment_episode ie 
          JOIN bed b ON (b.id = ie.bed_id)
          JOIN room r ON (r.id = b.room_id)
          JOIN sector s ON (s.id = r.sector_id)
          JOIN patient_discharge pd ON (pd.internment_episode_id = ie.id)
          WHERE s.care_type_id = 3 
          AND s.sector_organization_id = 2
          AND s.sector_type_id = 2
      )
      SELECT DISTINCT ON (hc.patient_id, hc.snomed_id) hc.patient_id, it.description AS identification_type, p2.identification_number, 
      CASE WHEN pe.name_self_determination IS NULL THEN CONCAT(p2.first_name, ' ', p2.middle_names) WHEN pe.name_self_determination IS NOT NULL THEN pe.name_self_determination END AS name, 
      CONCAT(p2.last_name, ' ', p2.other_last_names) AS last_name, g.description AS sex, 
      spg.description AS gender, TO_CHAR(p2.birth_date, 'DD/MM/YYYY') AS birth_date, p3.description AS state, d.description AS department, c.description AS city, CONCAT(a.street,' ', a."number") AS address, 
      CASE WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y') IS NULL THEN '< 1' ELSE SUBSTRING(patient_age_period FROM 'P(.*?)Y') END AS consultation_patient_age, ia.state AS institution_state, ia.department AS institution_department, 
      ia.city AS institution_city, ia.name AS institution, ia.sisa_code, TO_CHAR(d2.created_on, 'DD/MM/YYYY') AS consultation_date, ew.epidemiological_week_number AS consultation_date_week,
      TO_CHAR(hc.start_date, 'DD/MM/YYYY') AS synthoms_start_date, CASE WHEN preg.id IS NULL THEN 'No' WHEN preg.id IS NOT NULL THEN 'Si' END AS pregnant, 
      CASE WHEN internment.id IS NULL THEN 'No' WHEN internment.id IS NOT NULL THEN 'Si' END AS internment, TO_CHAR(internment.created_on, 'DD/MM/YYYY') AS internment_date,
      CASE WHEN uti.id IS NULL THEN 'No' WHEN uti.id IS NOT NULL THEN 'Si' END AS uti_internment, CASE WHEN internment.discharge_type_id = 3 THEN 'Si' ELSE 'No' END AS died, 
      TO_CHAR(internment.administrative_discharge_date, 'DD/MM/YYYY') AS discharge_date, s.pt AS snomed_concept, sd.cie10_codes, sd.cie10_event, 
      CASE WHEN sd.numeric_notification IS TRUE THEN 'Si' ELSE 'No' END AS numeric_notification,
      CASE WHEN sd.individual_notification IS TRUE THEN 'Si' ELSE 'No' END AS individual_notification,
      CASE WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y') IS NULL AND (SUBSTRING(patient_age_period, 'P(.*?)M')::INTEGER IS NULL OR SUBSTRING(patient_age_period, 'P(.*?)M')::INTEGER < 6) THEN '< 6 m' 
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y') IS NULL AND SUBSTRING(patient_age_period, 'P(.*?)M')::INTEGER BETWEEN 6 AND 11 THEN '6 a 11 m'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER = 1 AND SUBSTRING(patient_age_period, 'Y(.*?)M')::INTEGER <= 11 THEN '12 a 23 m'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 2 AND 4 THEN '2 a 4'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 5 AND 9 THEN '5 a 9'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 10 AND 14 THEN '10 a 14'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 15 AND 19 THEN '15 a 19'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 20 AND 24 THEN '20 a 24'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 25 AND 34 THEN '25 a 34'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 35 AND 44 THEN '35 a 44'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 45 AND 64 THEN '45 a 64'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER BETWEEN 65 AND 74 THEN '65 a 74'
      WHEN SUBSTRING(patient_age_period FROM 'P(.*?)Y')::INTEGER >= 75 THEN '>= a 75'
      END AS age_range, ia.id as institution_id
      FROM health_condition hc
      JOIN document_health_condition dhc ON (dhc.health_condition_id = hc.id)
      JOIN document d2 ON (d2.id = dhc.document_id)
      JOIN snomed s ON s.id = hc.snomed_id
      JOIN patient p ON p.id = hc.patient_id
      JOIN person p2 ON p2.id = p.person_id
      LEFT JOIN identification_type it ON (it.id = p2.identification_type_id)
      JOIN snvs_diagnose sd ON (sd.sctid = s.sctid)
      LEFT JOIN gender g ON (p2.gender_id = g.id)
      LEFT JOIN person_extended pe ON (pe.person_id = p2.id)
      LEFT JOIN self_perceived_gender spg ON (spg.id = pe.gender_self_determination)
      LEFT JOIN address a ON (a.id = pe.address_id)
      LEFT JOIN province p3 ON (p3.id = a.province_id)
      LEFT JOIN lc ON (lc.patient_id = hc.patient_id AND lc.snomed_id = hc.snomed_id)
      LEFT JOIN department d ON (d.id = a.department_id)
      LEFT JOIN city c ON (c.id = a.city_id)
      LEFT JOIN ia ON (ia.id = d2.institution_id)
      LEFT JOIN epidemiological_week ew ON (d2.created_on BETWEEN ew.start_date AND ew.end_date)
      LEFT JOIN preg ON (d2.patient_id = preg.patient_id AND d2.created_on BETWEEN preg.created_on AND preg.created_on + INTERVAL '9 months')
      LEFT JOIN uti ON (d2.patient_id = uti.patient_id AND d2.created_on BETWEEN uti.created_on AND COALESCE(uti.administrative_discharge_date, DATE 'infinity'))
      LEFT JOIN internment ON (internment.patient_id = d2.patient_id AND d2.created_on BETWEEN internment.created_on AND COALESCE(internment.administrative_discharge_date, DATE 'infinity'))
      WHERE hc.status_id = '55561003'
      AND hc.problem_id IN ('439401001', '55607006', '-55607006')
      AND hc.verification_status_id = '59156000'
      AND (hc.id = (SELECT id
                      FROM health_condition hc2
                      WHERE hc2.patient_id = hc.patient_id
                      AND hc2.snomed_id = hc.snomed_id
                      AND hc2.created_on > lc.created_on
                      AND hc2.status_id = '55561003'
                      AND hc2.problem_id IN ('439401001', '55607006', '-55607006')
                      AND hc2.verification_status_id = '59156000'
                      ORDER BY hc2.created_on DESC
                      LIMIT 1)
      OR lc.id IS NULL)
      ORDER BY hc.patient_id, hc.snomed_id, d2.created_on`,
    measures: {
        cantidad_casos_reporte_epidemiologico: {
            sql: `*`,
            type: `count`,
            title: `Cantidad de casos`
        }
    },
    dimensions: {
        id_paciente: {
            sql: `patient_id`,
            type: `number`,
            title: `Paciente`
        },
        tipo_identificacion: {
            sql: `identification_type`,
            type: `string`,
            title: `Tipo Identificación`
        },
        identificacion: {
            sql: `identification_number`,
            type: `string`,
            title: `N° identificación`
        },
        nombre: {
            sql: `name`,
            type: `string`,
            title: `Nombre`
        },
        apellido: {
            sql: `last_name`,
            type: `string`,
            title: `Apellido`
        },
        sexo: {
            sql: `sex`,
            type: `string`,
            title: `Sexo`
        },
        genero: {
            sql: `gender`,
            type: `string`,
            title: `Género`
        },
        fecha_nacimiento: {
            sql: `birth_date`,
            type: `string`,
            title: `Fecha Nac.`
        },
        provincia_paciente: {
            sql: `state`,
            type: `string`,
            title: `Provincia`
        },
        departamento_paciente: {
            sql: `department`,
            type: `string`,
            title: `Departamento`
        },
        ciudad_paciente: {
            sql: `city`,
            type: `string`,
            title: `Ciudad`
        },
        domicilio: {
            sql: `address`,
            type: `string`,
            title: `Domicilio`
        },
        edad_paciente_consulta: {
            sql: `consultation_patient_age`,
            type: `string`,
            title: `Edad durante consulta`
        },
        rango_etario: {
            sql: `age_range`,
            type: `string`,
            title: `Rango etario durante consulta`
        },
        provincia_institucion: {
            sql: `institution_state`,
            type: `string`,
            title: `Provincia institución`
        },
        departamento_institucion: {
            sql: `institution_department`,
            type: `string`,
            title: `Departamento institución`
        },
        ciudad_institucion: {
            sql: `institution_city`,
            type: `string`,
            title: `Ciudad institución`
        },
        nombre_institucion: {
            sql: `institution`,
            type: `string`,
            title: `Nombre institución`
        },
        codigo_sisa: {
            sql: `sisa_code`,
            type: `string`,
            title: `Código SISA`
        },
        fecha_consulta: {
            sql: `consultation_date`,
            type: `string`,
            title: `Fecha consulta`
        },
        semana_epidemiologica_consulta: {
            sql: `consultation_date_week`,
            type: `number`,
            title: `Semana epidemiológica`
        },
        fecha_comienzo_sintomas: {
            sql: `synthoms_start_date`,
            type: `string`,
            title: `Fecha inicio síntomas`
        },
        en_embarazo: {
            sql: `pregnant`,
            type: `string`,
            title: `Duramente embarazo`
        },
        en_internacion: {
            sql: `internment`,
            type: `string`,
            title: `Durante internación`
        },
        fecha_internacion: {
            sql: `internment_date`,
            type: `string`,
            title: `Fecha internación`
        },
        en_uti: {
            sql: `uti_internment`,
            type: `string`,
            title: `Internado en UTI`
        },
        fallecido: {
            sql: `died`,
            type: `string`,
            title: `Falleció por el problema`
        },
        fecha_alta: {
            sql: `discharge_date`,
            type: `string`,
            title: `Fecha alta/fallecimiento`
        },
        concepto_snomed: {
            sql: `snomed_concept`,
            type: `string`,
            title: `Concepto SNOMED`
        },
        codigo_cie10: {
            sql: `cie10_codes`,
            type: `string`,
            title: `Códigos CIE10`
        },
        evento_cie10: {
            sql: `cie10_event`,
            type: `string`,
            title: `Evento CIE10`
        },
        notificacion_numerica: {
            sql: `numeric_notification`,
            type: `string`,
            title: `Notificación numérica`
        },
        notificacion_individual: {
            sql: `individual_notification`,
            type: `string`,
            title: `Notificación individual`
        },
        institucion: {
            sql: `institution_id`,
            type: `number`,
            title: `Institución`
        }
    },
    title:` `,
    dataSource: `default`
});