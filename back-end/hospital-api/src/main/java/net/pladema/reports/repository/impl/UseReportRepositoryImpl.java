package net.pladema.reports.repository.impl;

import net.pladema.reports.repository.UseReportRepository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UseReportRepositoryImpl implements UseReportRepository {
	private final EntityManager entityManager;

	public UseReportRepositoryImpl(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}

	@Override
	public List<Map<String, Object>> getImplementedInstitutions() {
		String sqlString = "SELECT i.sisa_code AS codigo_refes, i.name AS nombre, c.description AS localidad, p.description AS provincia " +
				"FROM institution i " +
				"JOIN address a ON (i.address_id = a.id) " +
				"LEFT JOIN city c ON (a.city_id = c.id) " +
				"LEFT JOIN department d ON (c.department_id = d.id) " +
				"LEFT JOIN province p ON (d.province_id = p.id)";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("codigo_refes", row[0].toString());
			aux.put("nombre_institucion", row[1].toString());
			aux.put("localidad", row[2].toString());
			aux.put("provincia", row[3].toString());
			result.add(aux);
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getUserSpecialty(Integer domain) {
		String sqlString = "SELECT cs.name, COUNT(DISTINCT u.id) AS cantidad_usuarios " +
				"FROM healthcare_professional_specialty hps " +
				"JOIN clinical_specialty cs ON (hps.clinical_specialty_id = cs.id) " +
				"JOIN professional_professions pp ON (hps.professional_profession_id = pp.id) " +
				"JOIN healthcare_professional hp ON (pp.healthcare_professional_id = hp.id) " +
				"JOIN user_person up ON (hp.person_id = up.person_id) " +
				"JOIN users u ON (up.user_id = u.id) " +
				"WHERE u.deleted = false " +
				"AND hps.deleted = false " +
				"AND pp.deleted = false " +
				"GROUP BY cs.name";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("especialidad", row[0].toString());
			aux.put("cantidad_usuarios", row[1].toString());
			aux.put("dominio", domain);
			result.add(aux);
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getInstitutionSpecialty() {
		String sqlString = "SELECT i.sisa_code AS codigo_refes, i.name AS nombre, cs.name AS especialidad, COUNT(DISTINCT u.id) AS cantidad_usuarios " +
				"FROM institution i " +
				"JOIN user_role ur ON (i.id = ur.institution_id) " +
				"JOIN user_person up ON (ur.user_id = up.user_id) " +
				"JOIN healthcare_professional hp ON (up.person_id = hp.person_id) " +
				"JOIN professional_professions pp ON (hp.id = pp.healthcare_professional_id) " +
				"JOIN healthcare_professional_specialty hps ON (pp.id = hps.professional_profession_id) " +
				"JOIN clinical_specialty cs ON (hps.clinical_specialty_id = cs.id) " +
				"JOIN users u ON (up.user_id = u.id) " +
				"WHERE u.deleted = false " +
				"AND ur.deleted = false " +
				"AND pp.deleted = false " +
				"AND hps.deleted = false " +
				"GROUP BY codigo_refes, nombre, especialidad " +
				"ORDER BY codigo_refes";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("codigo_refes", row[0].toString());
			aux.put("nombre_institucion", row[1].toString());
			aux.put("especialidad", row[2].toString());
			aux.put("cantidad_usuarios", row[3].toString());
			result.add(aux);
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getInstitutionConsultation(String limitDate) {
		String sqlString = "SELECT i.sisa_code AS codigo_refes, i.name AS nombre, COUNT(1) AS cantidad_consultas " +
				"FROM outpatient_consultation oc " +
				"JOIN institution i ON (oc.institution_id = i.id) " +
				"WHERE oc.start_date <= '" + limitDate + "' " +
				"GROUP BY codigo_refes, nombre";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("codigo_refes", row[0].toString());
			aux.put("nombre_institucion", row[1].toString());
			aux.put("especialidad_consultas", row[2].toString());
			aux.put("fecha_limite", limitDate);
			result.add(aux);
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getGivenAppointment(String limitDate) {
		String sqlString = "SELECT i.sisa_code AS codigo_refes, i.name AS nombre, COUNT(a.id) AS cantidad_turnos_generados " +
				"FROM appointment a " +
				"JOIN appointment_assn aa ON (a.id = aa.appointment_id) " +
				"JOIN diary d ON (aa.diary_id = d.id) " +
				"JOIN doctors_office do2 ON (d.doctors_office_id = do2.id) " +
				"JOIN institution i ON (do2.institution_id = i.id) " +
				"WHERE a.created_on <= '" + limitDate + "' " +
				"GROUP BY codigo_refes, nombre";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("codigo_refes", row[0].toString());
			aux.put("nombre_institucion", row[1].toString());
			aux.put("cantidad_turnos_generados", row[2].toString());
			aux.put("fecha_limite", limitDate);
			result.add(aux);
		});
		return result;
	}

	@Override
	public List<Map<String, Object>> getConfirmedOrAttendedAppointment(String limitDate) {
		String sqlString = "SELECT i.sisa_code AS codigo_refes, i.name AS nombre, COUNT(DISTINCT a.id) AS cantidad_turnos_confirmados_antedidos " +
				"FROM appointment a " +
				"JOIN appointment_assn aa ON (a.id = aa.appointment_id) " +
				"JOIN diary d ON (aa.diary_id = d.id) " +
				"JOIN doctors_office do2 ON (d.doctors_office_id = do2.id) " +
				"JOIN institution i ON (do2.institution_id = i.id) " +
				"WHERE a.created_on <= '" + limitDate + "' " +
				"AND a.appointment_state_id IN (2,4) " +
				"GROUP BY codigo_refes, nombre";

		List<Map<String, Object>> result = new ArrayList<>();

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString).getResultList();

		queryResult.forEach(row -> {
			Map<String, Object> aux = new HashMap<>();
			aux.put("codigo_refes", row[0].toString());
			aux.put("nombre_institucion", row[1].toString());
			aux.put("cantidad_turnos_confirmados_antedidos", row[2].toString());
			aux.put("fecha_limite", limitDate);
			result.add(aux);
		});
		return result;
	}

}
