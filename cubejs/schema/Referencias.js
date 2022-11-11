cube(`Referencias`, {
sql: `SELECT r.id,
        concat_ws(' ', it.description, p.identification_number) AS documento,
        concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), concat_ws(' ', p.first_name, p.middle_names)) AS paciente,
        concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), CASE WHEN pex.name_self_determination IS NULL THEN concat_ws(' ', p.first_name, p.middle_names) ELSE pex.name_self_determination END) AS paciente_auto_det,
        concat_ws('- ', pex.phone_prefix, pex.phone_number) AS telefono,
        pex.email,
        oc.institution_id as institucion_origen_id,
        io.name as institucion_origen,
        oc.doctor_id,
        concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), concat_ws(' ', doc.first_name, doc.middle_names)) AS profesional_solicitante,
        concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN docex.name_self_determination IS NULL THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE docex.name_self_determination END) AS profesional_auto_det,
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
        JOIN person doc ON (hp.person_id = doc.id)
        JOIN person_extended docex ON (docex.person_id = doc.id)
        JOIN patient pa ON (pa.id=oc.patient_id)
        JOIN person p ON (p.id=pa.person_id)
        JOIN person_extended pex ON (pex.person_id = p.id)
        JOIN identification_type it ON (p.identification_type_id = it.id)
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
      AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()})` : `WHERE r.id IS NULL`}
UNION ALL
    SELECT r.id,
        concat_ws(' ', it.description, p.identification_number) AS documento,
        concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), concat_ws(' ', p.first_name, p.middle_names)) AS paciente,
        concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), CASE WHEN pex.name_self_determination IS NULL THEN concat_ws(' ', p.first_name, p.middle_names) ELSE pex.name_self_determination END) AS paciente_auto_det,
        concat_ws('- ', pex.phone_prefix, pex.phone_number) AS telefono,
        pex.email,
        oc.institution_id as institucion_origen_id,
        io.name as institucion_origen,
        oc.doctor_id,
        concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), concat_ws(' ', doc.first_name, doc.middle_names)) AS profesional_solicitante,
        concat_ws(', ', concat_ws(' ', doc.last_name, doc.other_last_names), CASE WHEN docex.name_self_determination IS NULL THEN concat_ws(' ', doc.first_name, doc.middle_names) ELSE docex.name_self_determination END) AS profesional_auto_det,
        cl.id as id_linea_cuidado,
        cl.description as linea_cuidado,
        cs.id as id_especialidad_clinica,
        cs.name as especialidad_clinica,
        oc.performed_date as fecha_consulta,
        r.destination_institution_id as institucion_destino_id,
        idest.name as institucion_destino,
        case when cr.id  is null then 'Referencia pendiente' else 'Contrarreferencia' end as tiene_contra
    FROM reference r 
        JOIN odontology_consultation oc ON (r.encounter_id = oc.id) 
        JOIN institution io ON (io.id = oc.institution_id)
        LEFT JOIN institution idest ON (idest.id = r.destination_institution_id)
        JOIN clinical_specialty cs ON (r.clinical_specialty_id = cs.id) 
        JOIN care_line cl ON (r.care_line_id = cl.id) 
        JOIN healthcare_professional hp ON (oc.doctor_id = hp.id) 
        JOIN person doc ON (hp.person_id = doc.id)
        JOIN person_extended docex ON (docex.person_id = doc.id)
        JOIN patient pa ON (pa.id=oc.patient_id)
        JOIN person p ON (p.id=pa.person_id)
        JOIN person_extended pex ON (pex.person_id = p.id)
        JOIN identification_type it ON (p.identification_type_id = it.id)
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
        type: `count`,
        title: `Cantidad Referencias`,
    }
  },
  
  dimensions: {
    // Id
    id: {
      sql: `id`,
      type: `string`,
      title: 'Id',
    },
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
     // Apellido y nombre autopercibido del paciente
     paciente_auto_det: {
      sql: `paciente_auto_det`,
      type: `string`,
      title: 'Paciente',
    },

    // teléfono del paciente
    telefono: {
      sql: `telefono`,
      type: `string`,
      title: 'Nro de teléfono',
    },
    // Email del paciente
    email: {
      sql: `email`,
      type: `string`,
      title: 'Email',
    },
    // Línea de cuidado
    id_linea_cuidado: {
      sql: `id_linea_cuidado`,
      type: `number`,
      title: 'Id línea cuidado',
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
      title: 'Id Especialidad',
      
    },
    especialidad_clinica: {
      sql: `especialidad_clinica`,
      type: `string`,
      title: 'Especialidad',
    },
    doctor_id: {
      sql: `doctor_id`,
      type: `number`,
      title: 'Id Profesional solicitante',
    },
    // Apellido y nombre del profesional solicitante
    profesional_solicitante: {
      sql: `profesional_solicitante`,
      type: `string`,
      title: `Profesional solicitante`,
    },
    // Apellido y nombre autopercibido del profesional solicitante
    profesional_auto_det: {
      sql: `profesional_auto_det`,
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
      title: 'Institución solicitante id',
    },
    institucion_origen: {
      sql: `institucion_origen`,
      type: `number`,
      title: 'Institución solicitante',
    },
    // Institución destino
    institucion_destino_id: {
      sql: `institucion_destino_id`,
      type: `number`,
      title: 'Institución destino id',
    },
    institucion_destino: {
      sql: `institucion_destino`,
      type: `number`,
      title: 'Institución destino',
    },
  },
  title:` `,
  dataSource: `default`
});
