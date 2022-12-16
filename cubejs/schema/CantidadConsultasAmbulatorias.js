cube(`CantidadConsultasAmbulatorias`, {
  sql: `SELECT 
            oc.id, oc.start_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad, doc.last_name as profesional
        FROM 
            outpatient_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
      UNION ALL
        SELECT 
            oc.id, oc.performed_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad, doc.last_name as profesional
        FROM 
            odontology_consultation oc
            JOIN clinical_specialty cs ON (oc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (oc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (oc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)
      UNION ALL
        SELECT 
            nc.id, nc.performed_date as fecha_consulta, g.description as gender, pe.birth_date, cs.name as especialidad, doc.last_name as profesional
        FROM 
            nursing_consultation nc
            JOIN clinical_specialty cs ON (nc.clinical_specialty_id = cs.id)
            JOIN healthcare_professional hp ON (nc.doctor_id = hp.id)
            JOIN person doc ON (hp.person_id = doc.id)
            JOIN patient pa ON (nc.patient_id = pa.id)
            JOIN person pe ON (pa.person_id = pe.id)
            JOIN gender g ON (pe.gender_id = g.id)`,
  
  measures: {
    cantidad_turnos_estado: {
        sql: `id`,
        type: `count`,
        title: `Cantidad de consultas Ambulatorias`
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
