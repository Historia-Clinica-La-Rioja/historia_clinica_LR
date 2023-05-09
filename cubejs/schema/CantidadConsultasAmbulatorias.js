cube(`CantidadConsultasAmbulatorias`, {
  sql: `SELECT 
            oc.id, 'Ambulatoria' as tipo, oc.start_date as fecha_consulta, g.description as gender, spg.description as self_perceived_gender, date_part('year', age(oc.created_on, pe.birth_date)) as age, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL OR pex.name_self_determination LIKE '' THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional_autopercibido,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), concat_ws(' ', doc.first_name, doc.middle_names)) AS profesional,
            oc.institution_id AS institucion_id
        FROM
            outpatient_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN person_extended pex ON (pex.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex_patient ON (pex_patient.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
            LEFT JOIN self_perceived_gender spg ON (pex_patient.gender_self_determination = spg.id)
        ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
          WHERE (oc.institution_id IN (
            SELECT ur.institution_id 
            FROM users as u 
            JOIN user_role ur on u.id = ur.user_id 
            WHERE ur.role_id = 8
            AND ur.deleted = false
            AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()}))` : ''}
    UNION ALL
        SELECT 
            oc.id, 'Odontología' as tipo, oc.performed_date as fecha_consulta, g.description as gender, spg.description as self_perceived_gender, date_part('year', age(oc.created_on, pe.birth_date)) as age, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL OR pex.name_self_determination LIKE '' THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional_autopercibido,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), concat_ws(' ', doc.first_name, doc.middle_names)) AS profesional,
            oc.institution_id AS institucion_id
        FROM 
            odontology_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN person_extended pex ON (pex.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex_patient ON (pex_patient.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
            LEFT JOIN self_perceived_gender spg ON (pex_patient.gender_self_determination = spg.id)
        ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
          WHERE (oc.institution_id IN (
            SELECT ur.institution_id 
            FROM users as u 
            JOIN user_role ur on u.id = ur.user_id 
            WHERE ur.role_id = 8
            AND ur.deleted = false
            AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()}))` : ''}
      UNION ALL
        SELECT 
            nc.id, 'Enfermería' as tipo, nc.performed_date as fecha_consulta, g.description as gender, spg.description as self_perceived_gender, date_part('year', age(nc.created_on, pe.birth_date)) as age, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL OR pex.name_self_determination LIKE '' THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional_autopercibido,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), concat_ws(' ', doc.first_name, doc.middle_names)) AS profesional,
            nc.institution_id AS institucion_id
        FROM 
            nursing_consultation nc
            JOIN clinical_specialty cs ON (nc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (nc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN person_extended pex ON (pex.person_id = doc.id)
            JOIN patient pa ON (nc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex_patient ON (pex_patient.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
            LEFT JOIN self_perceived_gender spg ON (pex_patient.gender_self_determination = spg.id)
        ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
          WHERE (nc.institution_id IN (
            SELECT ur.institution_id 
            FROM users as u 
            JOIN user_role ur on u.id = ur.user_id 
            WHERE ur.role_id = 8
            AND ur.deleted = false
            AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()}))` : ''}`,
  
  measures: {
    cantidad_turnos_estado: {
        sql: `id`,
        type: `count`,
        title: ` `
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
      title: 'Fecha de realizacion',
    },
    // Génereo
    gender: {
      sql: `gender`,
      type: `string`,
      title: 'Género',
    },
    // Género autopercibido
    self_perceived_gender: {
      sql: `self_perceived_gender`,
      type: `string`,
      title: 'Identidad de género'
    },
    // Tipo
    tipo: {
      sql: `tipo`,
      type: `string`,
      title: 'Tipo de consulta',
    },
    // Especialidad
    especialidad: {
      sql: `especialidad`,
      type: `string`,
      title: 'Especialidad',
    },
    // Doctor
    profesional: {
      sql: `profesional`,
      type: `string`,
      title: 'Doctor',
    },
    // Doctor
    profesional_autopercibido: {
      sql: `profesional_autopercibido`,
      type: `string`,
      title: 'Doctor',
    },
    // Institución
    institucion_id: {
      sql: `institucion_id`,
      type: `number`,
      title: 'Institución',
    },
    // Rango etario
    age_range: {
      sql: `CASE 
      WHEN age BETWEEN 0 AND 3 THEN '0-3'
      WHEN age BETWEEN 3 AND 11 THEN '03-11'
      WHEN age BETWEEN 11 AND 17 THEN '11-17'
      WHEN age BETWEEN 17 AND 29 THEN '18-29'
      WHEN age BETWEEN 29 AND 39 THEN '30-39'
      WHEN age BETWEEN 39 AND 49 THEN '40-49'
      WHEN age BETWEEN 49 AND 59 THEN '50-59'
      ELSE '>60'
    END`,
      type: `string`,
      title: 'Rango etario',
    },
    },
  title:` `,
  dataSource: `default`
});
