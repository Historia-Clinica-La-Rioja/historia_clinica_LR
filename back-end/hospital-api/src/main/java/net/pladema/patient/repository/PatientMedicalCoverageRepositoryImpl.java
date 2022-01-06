package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.PrivateHealthInsuranceDetails;
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
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliateNumber, pmc.vigencyDate, pmc.active, mc.id as mcid, mc.name, mc.cuit, hi.rnos, hi.acronym, phi.plan, phid " +
				"FROM PatientMedicalCoverageAssn pmc " +
				"JOIN MedicalCoverage mc ON (pmc.medicalCoverageId = mc.id) " +
				"LEFT JOIN HealthInsurance hi ON (mc.id = hi.id) " +
				"LEFT JOIN PrivateHealthInsurance phi ON (mc.id = phi.id) "+
				"LEFT JOIN PrivateHealthInsuranceDetails phid ON (pmc.privateHealthInsuranceDetailsId = phid.id) "+
				"WHERE pmc.active = true " +
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
								(Integer) h[7],
								(String) h[8],
								(String) h[9],
								(PrivateHealthInsuranceDetails) h[10]))
		);
		return result;
	}


	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id, mc.name, mc.cuit, hi.rnos, hi.acronym" +
				"FROM patient_medical_coverage pmc " +
				"JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"JOIN health_insurance hi ON (mc.id = hi.id) " +
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
								(Integer) h[7],
								(String) h[8]))
		);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientPrivateHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id as mcid, mc.name, mc.cuit, phid.id as phid, phid.start_date, phid.end_date, phi.plan " +
				"FROM patient_medical_coverage pmc " +
				"JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"JOIN private_health_insurance phi ON (mc.id = phi.id) " +
				"JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
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
								new PrivateHealthInsuranceDetails((Integer) h[7], h[8] != null ? ((Date) h[8]).toLocalDate() : null, h[9] != null ? ((Date) h[9]).toLocalDate() : null),
								(String) h[10]))

		);
		return result;
	}


}
