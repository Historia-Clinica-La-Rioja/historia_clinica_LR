package net.pladema.violencereport.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ViolenceReportRepository extends SGXAuditableEntityJPARepository<ViolenceReport, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT MAX(vr.situationId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId")
	Integer getPatientLastSituationId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(" SELECT vr.id " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId " +
			"AND vr.situationId = :situationId")
	List<Integer> getAllReportIdsByPatientIdAndSituationId(@Param("patientId") Integer patientId, @Param("situationId") Short situationId);

	@Transactional(readOnly = true)
	@Query(value = " WITH last_patient_situation_evolution AS (" +
			"SELECT vr.situation_id, vr.patient_id , MAX(vr.evolution_id) AS last_evolution_id " +
			"FROM {h-schema}violence_report vr " +
			"GROUP BY vr.situation_id, vr.patient_id)" +
			"SELECT vr.id " +
			"FROM {h-schema}violence_report vr " +
			"JOIN last_patient_situation_evolution lpse ON (lpse.situation_id = vr.situation_id AND lpse.patient_id = vr.patient_id AND lpse.last_evolution_id = vr.evolution_id) " +
			"WHERE vr.patient_id = :patientId " +
			"AND vr.situation_id = :situationId", nativeQuery = true)
    Integer getLastSituationEvolutionReportId(@Param("patientId") Integer patientId, @Param("situationId") Integer situationId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceReportBo(vr.canReadAndWrite, vr.hasIncome, vr.worksAtFormalSector, vr.hasSocialPlan, " +
			"vr.hasDisability, vr.disabilityCertificateStatusId, vr.isInstitutionalized, vr.institutionalizedDetails, vr.lackOfLegalCapacity, vr.coordinationInsideHealthSector, " +
			"vr.coordinationWithinHealthSystem, vr.coordinationWithinHealthInstitution, vr.internmentIndicatedStatusId, vr.coordinationWithOtherSocialOrganizations, " +
			"vr.werePreviousEpisodeWithVictimOrKeeper, vr.institutionReported, vr.wasSexualViolence, vr.observations) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.id = :reportId")
	ViolenceReportBo getViolenceReportDataWithoutEpisodeById(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT MAX(vr.evolutionId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId " +
			"AND vr.situationId = :situationId")
	Short getSituationLastEvolutionId(@Param("patientId") Integer patientId, @Param("situationId") Short situationId);

}
