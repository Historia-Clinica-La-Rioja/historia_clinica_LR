package net.pladema.patient.repository;

import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class PatientMedicalCoverageRepositoryImpl implements PatientMedicalCoverageRepositoryCustom{

	@PersistenceContext
	private EntityManager entityManager;

	public PatientMedicalCoverageRepositoryImpl(EntityManager entityManager){
		super();
		this.entityManager = entityManager;
	}

	/*@Override
	@Transactional(readOnly = true)
	public PatientMedicalCoverageVo getPatientCoverage(Integer patientMedicalCoverageId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id, mc.name, hi.rnos, hi.acronym, phi.plan, phid.id as phid, phid.start_date, phid.end_date " +
				"FROM patient_medical_coverage pmc " +
				"JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"LEFT JOIN health_insurance hi ON (mc.id = hi.id) " +
				"LEFT JOIN private_health_insurance phi ON (mc.id = phi.id) "+
				"LEFT JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
				"WHERE pmc.active = true " +
				"AND pmc.id = :patientMedicalCoverageId ";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("patientMedicalCoverageId", patientMedicalCoverageId)
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
								(Integer) h[6],
								(String) h[7],
								(String) h[8],
								(Integer) h[9],
								h[10] != null ? ((Date) h[10]).toLocalDate() : null,
								h[11] != null ? ((Date) h[11]).toLocalDate() : null))
		);
		if (result != null)
			return result.get(0);
		return
	}*/

	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientCoverages(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id, mc.name, hi.rnos, hi.acronym, phi.plan, phid.id as phid, phid.start_date, phid.end_date " +
				"FROM patient_medical_coverage pmc " +
				"JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"LEFT JOIN health_insurance hi ON (mc.id = hi.id) " +
				"LEFT JOIN private_health_insurance phi ON (mc.id = phi.id) "+
				"LEFT JOIN private_health_insurance_details phid ON (pmc.private_health_insurance_details_id = phid.id) "+
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
								(Integer) h[6],
								(String) h[7],
								(String) h[8],
								(Integer) h[9],
								h[10] != null ? ((Date) h[10]).toLocalDate() : null,
								h[11] != null ? ((Date) h[11]).toLocalDate() : null))
		);
		return result;
	}


	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id, mc.name, hi.rnos, hi.acronym " +
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
								(Integer) h[6],
								(String) h[7]))
		);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientMedicalCoverageVo> getActivePatientPrivateHealthInsurances(Integer patientId) {
		String sqlString = "SELECT pmc.id as pmcid, pmc.affiliate_number, pmc.vigency_date, pmc.active, mc.id as mcid, mc.name, phid.id as phid, phid.start_date, phid.end_date, phi.plan " +
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
								(Integer) h[6],
								h[7] != null ? ((Date) h[7]).toLocalDate() : null,
								h[8] != null ? ((Date) h[8]).toLocalDate() : null,
								(String) h[9]))

		);
		return result;
	}


}
