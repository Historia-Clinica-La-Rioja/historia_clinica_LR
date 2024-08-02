cube(`TablaTurnosCentroLlamado`, {
    sql: `
    	SELECT bp.identification_number, UPPER(bp.last_name) AS patient_last_name, UPPER(bp.first_name) AS patient_first_name, UPPER(g.description) AS gender, 
	TO_CHAR(a.date_type_id, 'DD/MM/YYYY') AS appointment_date, TO_CHAR(a."hour", 'HH24:MIhs') AS appointment_time, UPPER(p.last_name) AS professional_last_name, 
	UPPER(p.first_name) AS professional_first_name, UPPER(COALESCE(cs."name", cs2."name", '-')) AS service, d2.description AS department_name, UPPER(i."name") AS institution_name
	FROM booking_appointment ba
	JOIN booking_person bp ON (ba.booking_person_id = bp.id)
	JOIN gender g ON (g.id = bp.gender_id)
	JOIN appointment a ON (a.id = ba.appointment_id)
	JOIN appointment_assn aa ON (aa.appointment_id = a.id)
	JOIN diary d ON (aa.diary_id = d.id)
	JOIN doctors_office do2 ON (do2.id = d.doctors_office_id)
	JOIN institution i ON (i.id = do2.institution_id)
	LEFT JOIN address a2 ON (a2.id = i.address_id)
	LEFT JOIN department d2 ON (d2.id = a2.department_id)
	JOIN healthcare_professional hp ON (hp.id = d.healthcare_professional_id)
	JOIN person p ON (p.id = hp.person_id)
	LEFT JOIN hierarchical_unit hu ON (hu.id = d.hierarchical_unit_id)
	LEFT JOIN hierarchical_unit hu2 ON (hu2.id = hu.closest_service_id)
	LEFT JOIN clinical_specialty cs ON (cs.id = hu.clinical_specialty_id)
	LEFT JOIN clinical_specialty cs2 ON (cs2.id = hu2.clinical_specialty_id)
	WHERE a.date_type_id >= CURRENT_DATE - 1
	AND a.appointment_state_id = 6 
	ORDER BY a.id DESC
    `,
    measures: {
        turnos_en_centro_llamado: {
            sql: `*`,
            type: `count`,
            title: `Turnos Centro Llamado`
        }
    },
    dimensions: {
        numero_identificacion: {
            sql: `identification_number`,
            type: `string`,
            title: `Número de identificación de paciente`
        },
        apellido_paciente: {
            sql: `patient_last_name`,
            type: `string`,
            title:`Apellido de paciente`
        },
        primer_nombre_paciente: {
            sql: `patient_first_name`,
            type: `string`,
            title: `Nombre de paciente`
        },
        genero: {
            sql: `gender`,
            type: `string`,
            title: `Género`
        },
        fecha_turno: {
            sql: `appointment_date`,
            type: `string`,
            title: `Fecha del turno`
        },
        hora_turno: {
            sql: `appointment_time`,
            type: `string`,
            title: `Hora del turno`
        },
        apellido_profesional: {
            sql: `professional_last_name`,
            type: `string`,
            title: `Apellido del profesional`
        },
        nombre_profesional: {
            sql: `professional_first_name`,
            type: `string`,
            title: `Nombre del profesional`
        },
        departamento: {
            sql: `department_name`,
            type: `string`,
            title: `Departamento`
        },
        servicio: {
            sql: `service`,
            type: `string`,
            title: `Servicio`
        },
        institucion: {
            sql: `institution_name`,
            type: `string`,
            title: `Intitución`
        }
    },
    title:` `,
    dataSource: `default`
});
