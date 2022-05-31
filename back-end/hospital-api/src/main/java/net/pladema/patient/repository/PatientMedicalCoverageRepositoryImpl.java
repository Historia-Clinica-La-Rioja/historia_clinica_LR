package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PatientMedicalCoverageRepositoryImpl implements PatientMedicalCoverageRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;

	public PatientMedicalCoverageRepositoryImpl(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientCoverages(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliateNumber, pmc.vigencyDate, pmc.active, mc.id as mcid, mc.name, mc.cuit, mc.type, hi.rnos, hi.acronym, pmc.conditionId, pmc.startDate, pmc.endDate, pmc.planId " +
				"FROM PatientMedicalCoverageAssn pmc " +
				"JOIN MedicalCoverage mc ON (pmc.medicalCoverageId = mc.id) " +
				"LEFT JOIN HealthInsurance hi ON (mc.id = hi.id) " +
				"LEFT JOIN PrivateHealthInsurance phi ON (mc.id = phi.id) "+
				"WHERE pmc.active = true " +
				"AND mc.deleteable.deleted = false " +
				"AND pmc.patientId = :patientId ";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("patientId", patientId)
				.getResultList();
		List<PatientMedicalCoverageVo> result = new ArrayList<>();
		queryResult.forEach(h ->
				result.add(
						new PatientMedicalCoverageVo(
								(Integer) h[0],
								(String) h[1],
								(LocalDate) h[2],
								(Boolean) h[3],
								(Integer) h[4],
								(String) h[5],
								(String) h[6],
								(Short) h[7],
								(Integer) h[8],
								(String) h[9],
								(Short)h[10],
								(LocalDate) h[11],
								(LocalDate) h[12],
								(Integer) h[13]))
		);
		return result;
	}


	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id, mc.name, mc.cuit, mc.type, hi.rnos, hi.acronym, pmc.conditionId, pmc.planId " +
				"FROM {h-schema}patient_medical_coverage pmc " +
				"JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"JOIN {h-schema}health_insurance hi ON (mc.id = hi.id) " +
				"WHERE pmc.active = true " +
				"AND pmc.patient_id = :patientId ";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("patientId", patientId)
				.getResultList();
		List<PatientMedicalCoverageVo> result = new ArrayList<>();
		queryResult.forEach(h ->
				result.add(
						new PatientMedicalCoverageVo(
								(Integer) h[0],
								(String) h[1],
								h[2] != null ? ((Date) h[2]).toLocalDate() : null,
								(Boolean) h[3],
								(Integer) h[4],
								(String) h[5],
								(String) h[6],
								(Short) h[7],
								(Integer) h[8],
								(String) h[9],
								(Short) h[10],
								(Integer) h[11]))
		);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientPrivateHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id as mcid, mc.name, mc.cuit, mc.type, pmc.conditionId, pmc.start_date, pmc.end_date, pmc.plan_id " +
				"FROM {h-schema}patient_medical_coverage pmc " +
				"JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"JOIN {h-schema}private_health_insurance phi ON (mc.id = phi.id) " +
				"WHERE pmc.active = true " +
				"AND pmc.patient_id = :patientId ";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("patientId", patientId)
				.getResultList();
		List<PatientMedicalCoverageVo> result = new ArrayList<>();
		queryResult.forEach(h ->
				result.add(
						new PatientMedicalCoverageVo(
								(Integer) h[0],
								(String) h[1],
								h[2] != null ? ((Date) h[2]).toLocalDate() : null,
								(Boolean) h[3],
								(Integer) h[4],
								(String) h[5],
								(String) h[6],
								(Short) h[7],
								(Short) h[8],
								(LocalDate) h[9],
								(LocalDate) h[10],
								(Integer) h[11]))

		);
		return result;
	}


}
