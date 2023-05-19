cube(`CantidadTurnosPacientes`, {
  sql: `SELECT 
          ap.id as id, COALESCE(has.changed_state_date, ap.created_on) as fecha_consulta, cs.name as especialidad, ap.is_overturn as es_sobreturno, aState.description as estado,
          dof.institution_id AS institucion_id, date_part('year', age(ap.created_on, p.birth_date)) as age
        FROM 
          appointment ap
          JOIN appointment_assn apss ON (apss.appointment_id = ap.id)
          LEFT JOIN historic_appointment_state has ON (has.appointment_id = ap.id)
		  LEFT JOIN appointment_state aState ON (ap.appointment_state_id = aState.id)
		  LEFT JOIN patient pa ON (pa.id = ap.patient_id) 
		  LEFT JOIN person p ON (pa.person_id = p.id)
		  JOIN diary d ON (apss.diary_id = d.id)
          JOIN doctors_office dof ON (d.doctors_office_id = dof.id)
          JOIN clinical_specialty cs ON (d.clinical_specialty_id = cs.id)
		WHERE (has.changed_state_date IS NULL OR has.changed_state_date = (
			SELECT MAX(has2.changed_state_date) FROM historic_appointment_state has2 
			WHERE has2.appointment_id = ap.id))
			AND ap.appointment_state_id in (1,2,3,4,5) 
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
        title: ` `
    }
  },
  
  dimensions: {
    // Fecha consulta
    fecha_consulta: {
      sql: `fecha_consulta`,
      type: `time`,
      title: 'Fecha consulta',
    },
    // Institución
    institucion_id: {
      sql: `institucion_id`,
      type: `number`,
      title: 'Institución',
    },
	// Estado
    estado: {
      sql: `estado`,
      type: `string`,
      title: 'Estado',
    },
	// Sobreturno
	es_sobreturno :{
		sql: `CASE 
      WHEN es_sobreturno = true THEN 'Si' ELSE 'No'
    END`,
		type:  `boolean`,
		title: 'Sobreturno'
  },
      // Especialidad
    especialidad: {
      sql: `especialidad`,
      type: `string`,
      title: 'especialidad',
    },
  // Rango etario
    age_range: {
      sql: `CASE 
      WHEN age BETWEEN 0 AND 3 THEN '0 - 3'
      WHEN age BETWEEN 3 AND 11 THEN '03 - 11'
      WHEN age BETWEEN 11 AND 17 THEN '11 - 17'
      WHEN age BETWEEN 17 AND 29 THEN '18 - 29'
      WHEN age BETWEEN 29 AND 39 THEN '30 - 39'
      WHEN age BETWEEN 39 AND 49 THEN '40 - 49'
      WHEN age BETWEEN 49 AND 59 THEN '50 - 59'
	  WHEN age BETWEEN 59 AND 69 THEN '60 - 69'
	  WHEN age BETWEEN 69 AND 79 THEN '70 - 79'
	  WHEN age BETWEEN 79 AND 89 THEN '80 - 89'
	  WHEN age BETWEEN 89 AND 99 THEN '90 - 99'
      ELSE '> 99'
    END`,
      type: `string`,
      title: 'Rango etario',
    }
  },
  title:` `,
  dataSource: `default`
});
