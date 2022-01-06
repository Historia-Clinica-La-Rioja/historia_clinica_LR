
cube(`Referencias`, {
  sql: `SELECT 
  r.id,
  oc.start_date as fecha_consulta,
  p2.first_name as nombre_paciente,
  p2.last_name as apellido_paciente,

  cl.id as id_linea_cuidado,
  cl.description as linea_cuidado,
  cs.id as id_especialidad_clinica,
  cs.name as especialidad_clinica,

  case when cr.id  is null then 'Referencia pendiente' else 'Contrarreferencia' end as tiene_contra,
  
  oc.doctor_id,
  p.first_name as nombre_doctor_solicitante,
  p.last_name as apellido_doctor_solicitante,
  oc.institution_id as institucion_origen_id
FROM reference r 
JOIN outpatient_consultation oc ON (r.encounter_id = oc.id) 
JOIN clinical_specialty cs ON (r.clinical_specialty_id = cs.id) 
JOIN care_line cl ON (r.care_line_id = cl.id) 
JOIN healthcare_professional hp ON (oc.doctor_id = hp.id) 
JOIN person p ON (hp.person_id = p.id)
join patient pat on (pat.id=oc.patient_id)
join person p2 on (p2.id=pat.person_id)
LEFT JOIN counter_reference cr ON (r.id = cr.reference_id)`,
  
  measures: {
    cant_referencia: {
        sql: `id`,
        type: `sum`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
    },
    // Nombre y apellido del paciente
    nombre_paciente: {
      sql: `nombre_paciente`,
      type: `string`,
      title: 'Nombre del paciente',
    },
    apellido_paciente: {
      sql: `apellido_paciente`,
      type: `string`,
      title: 'Apellido del paciente',
    },
    // Línea de cuidado
    id_linea_cuidado: {
      sql: `id_linea_cuidado`,
      type: `number`,
    },
    linea_cuidado: {
      sql: `linea_cuidado`,
      type: `string`,
      title: 'Línea de cuidado',
    },
    // Especialidad
    id_especialidad_clinica: {
      sql: `id_especialidad_clinica`,
      type: `number`,
    },
    especialidad_clinica: {
      sql: `especialidad_clinica`,
      type: `string`,
      title: 'Especialidad',
    },
    // Nombre y apellido del profesional solicitante
    doctor_id: {
      sql: `doctor_id`,
      type: `number`,
    },
    nombre_doctor_solicitante: {
      sql: `nombre_doctor_solicitante`,
      type: `string`,
      title: `Nombre del profesional solicitante`,
    },
    apellido_doctor_solicitante: {
      sql: `apellido_doctor_solicitante`,
      type: `string`,
      title: `Apellido del profesional solicitante`,
    },
    // Estado referencia
    tiene_contra: {
      sql: `tiene_contra`,
      type: `number`,
      title: 'Estado referencia',
    },
    // Institución origen
    institucion_origen_id: {
      sql: `institucion_origen_id`,
      type: `number`,
    },
  },
  title:` `,
  dataSource: `default`
});
