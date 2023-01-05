cube(`CantidadConsultasAmbulatorias`, {
  sql: `SELECT 
            oc.id, 'Ambulatoria' as tipo, oc.start_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional
        FROM 
            outpatient_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex ON (pex.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
      UNION ALL
        SELECT 
            oc.id, 'Odontología' as tipo, oc.performed_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional
        FROM 
            odontology_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex ON (pex.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
      UNION ALL
        SELECT 
            nc.id, 'Enfermería' as tipo, nc.performed_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad,
            concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN pex.name_self_determination IS NULL THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE pex.name_self_determination END) AS profesional
        FROM 
            nursing_consultation nc
            JOIN clinical_specialty cs ON (nc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (nc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (nc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN person_extended pex ON (pex.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)`,
  
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
  },
  title:` `,
  dataSource: `default`
});
