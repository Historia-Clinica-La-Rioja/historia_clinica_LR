cube(`CantidadTurnosTotal`, {
  sql: `SELECT 
          ap.id, ap.appointment_state_id as estado, ap.date_type_id as fecha_consulta, cs.name as especialidad,
          concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), CASE WHEN pex.name_self_determination IS NULL OR pex.name_self_determination LIKE '' THEN concat_ws(' ', p.first_name, p.middle_names) ELSE pex.name_self_determination END) AS profesional_autopercibido,
          concat_ws(', ', concat_ws(' ', p.last_name, p.other_last_names), concat_ws(' ', p.first_name, p.middle_names)) AS profesional,
          dof.institution_id AS institucion_id
      FROM 
          appointment ap
      JOIN appointment_assn apss ON (apss.appointment_id = ap.id)
      JOIN diary d ON (apss.diary_id = d.id)
      JOIN doctors_office dof ON (d.doctors_office_id = dof.id)
      JOIN healthcare_professional hp ON (d.healthcare_professional_id = hp.id)
      JOIN person p ON (hp.person_id = p.id)
      JOIN person_extended pex ON (pex.person_id = p.id)
      JOIN clinical_specialty cs ON (d.clinical_specialty_id = cs.id)
      WHERE ap.appointment_state_id !=2
  ${SECURITY_CONTEXT.userId.unsafeValue() ? '' +  `
    AND (dof.institution_id IN (
      SELECT ur.institution_id 
      FROM users as u 
      JOIN user_role ur on u.id = ur.user_id 
      WHERE ur.role_id = 8
      AND ur.deleted = false
      AND u.id = ${SECURITY_CONTEXT.userId.unsafeValue()}))` : ''}`,
  
  measures: {
    cantidad_turnos: {
        sql: `id`,
        type: `count`,
        title: `Cantidad de turnos total`
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
      title: 'Fecha consulta',
    },
    // Especialidad
    especialidad: {
      sql: `especialidad`,
      type: `string`,
      title: 'especialidad',
    },
    // Profesional
    profesional: {
      sql: `profesional`,
      type: `string`,
      title: 'profesional',
    },
    profesional_autopercibido: {
      sql: `profesional_autopercibido`,
      type: `string`,
      title: 'profesional',
    },
    // Estado
    estado: {
      sql: `estado`,
      type: `string`,
      title: 'estado',
    },
    // Institución
    institucion_id: {
      sql: `institucion_id`,
      type: `number`,
      title: 'Institución',
    },
  },
  title:` `,
  dataSource: `default`
});
