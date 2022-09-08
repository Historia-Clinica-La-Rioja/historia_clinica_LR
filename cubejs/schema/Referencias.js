cube(`Referencias`, {
sql: `SELECT r.id,
        concat_ws(' ', it.description, p2.identification_number) AS documento,
        concat_ws(', ', concat_ws(' ', p2.last_name, p2.other_last_names), concat_ws(' ', p2.first_name, p2.middle_names)) AS paciente,
        oc.institution_id as institucion_origen_id,
        io.name as institucion_origen,
        oc.doctor_id,
        concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), concat_ws(' ', p.first_name, p.middle_names)) AS profesional_solicitante,
        cl.id as id_linea_cuidado,
        cl.description as linea_cuidado,
        cs.id as id_especialidad_clinica,
        cs.name as especialidad_clinica,
        oc.start_date as fecha_consulta,
        r.destination_institution_id as institucion_destino_id,
        idest.name as institucion_destino,
        case when cr.id  is null then 'Referencia pendiente' else 'Contrarreferencia' end as tiene_contra
      FROM reference r 
        JOIN outpatient_consultation oc ON (r.encounter_id = oc.id)
        JOIN institution io ON (io.id = oc.institution_id)
        LEFT JOIN institution idest ON (idest.id = r.destination_institution_id)
        JOIN clinical_specialty cs ON (r.clinical_specialty_id = cs.id) 
        JOIN care_line cl ON (r.care_line_id = cl.id) 
        JOIN healthcare_professional hp ON (oc.doctor_id = hp.id) 
        JOIN person p ON (hp.person_id = p.id)
        JOIN patient pat ON (pat.id=oc.patient_id)
        JOIN person p2 ON (p2.id=pat.person_id)
        JOIN identification_type it ON (p2.identification_type_id = it.id)
        LEFT JOIN counter_reference cr ON (r.id = cr.reference_id)
    ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    WHERE oc.institution_id IN (
      SELECT ur.institution_id 
      FROM users as u 
      JOIN user_role ur on u.id = ur.user_id 
      WHERE ur.role_id = 5 
      AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})
    OR r.destination_institution_id IN (
      SELECT ur.institution_id 
      FROM users as u 
      JOIN user_role ur on u.id = ur.user_id 
      WHERE ur.role_id = 5 
      AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}`,

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
      title: 'Fecha consulta',
    },
    // Tipo y número de documento
    documento: {
      sql: `documento`,
      type: `string`,
      title: 'Documento',
    },
    // Apellido y nombre del paciente
    paciente: {
      sql: `paciente`,
      type: `string`,
      title: 'Paciente',
    },
    // Línea de cuidado
    id_linea_cuidado: {
      sql: `id_linea_cuidado`,
      type: `number`,
      title: 'id linea cuidado',
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
      title: 'id Especialidad',
      
    },
    especialidad_clinica: {
      sql: `especialidad_clinica`,
      type: `string`,
      title: 'Especialidad',
    },
    doctor_id: {
      sql: `doctor_id`,
      type: `number`,
      title: 'id Profesional solicitante',
    },
    // Apellido y nombre del profesional solicitante
    doctor_solicitante: {
      sql: `profesional_solicitante`,
      type: `string`,
      title: `Profesional solicitante`,
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
      title: 'Institucion solicitante id',
    },
    institucion_origen: {
      sql: `institucion_origen`,
      type: `number`,
      title: 'Institucion solicitante',
    },
    // Institución destino
    institucion_destino_id: {
      sql: `institucion_destino_id`,
      type: `number`,
      title: 'Institucion destino id',
    },
    institucion_destino: {
      sql: `institucion_destino`,
      type: `number`,
      title: 'Institucion destino',
    },
  },
  title:` `,
  dataSource: `default`
});
