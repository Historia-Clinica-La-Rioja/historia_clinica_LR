package net.pladema.violencereport.infrastructure.output.repository;

import ar.lamansys.sgh.shared.domain.FilterOptionBo;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.violencereport.domain.ViolenceEpisodeDetailBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.domain.ViolenceReportSituationEvolutionBo;
import net.pladema.violencereport.infrastructure.output.repository.entity.ViolenceReport;

import org.springframework.data.jpa.repository.Modifying;
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

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW net.pladema.violencereport.domain.ViolenceReportSituationEvolutionBo(vr.situationId, vr.evolutionId, vr.episodeDate, vr.creationable.createdOn, up.pk.personId) " +
			"FROM ViolenceReport vr " +
			"JOIN UserPerson up on (up.pk.userId = vr.creationable.createdBy) " +
			"JOIN ViolenceModality vm ON (vm.pk.reportId = vr.id) " +
			"JOIN ViolenceType vt ON (vt.pk.reportId = vr.id) " +
			"WHERE vr.patientId = :patientId " +
			"AND (:institutionId IS NULL OR vr.institutionId = :institutionId) " +
			"AND (:modalityId IS NULL OR vm.pk.snomedId = :modalityId) " +
			"AND (:typeId IS NULL OR vt.pk.snomedId = :typeId) " +
			"AND (:situationId IS NULL OR vr.situationId = :situationId) " +
			"ORDER BY vr.creationable.createdOn DESC")
    List<ViolenceReportSituationEvolutionBo> getPatientHistoric(@Param("patientId") Integer patientId,
																@Param("institutionId") Integer institutionId,
																@Param("situationId") Short situationId,
																@Param("modalityId") Integer modalityId,
																@Param("typeId") Integer typeId);

	@Transactional(readOnly = true)
	@Query(" SELECT vr.id " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId " +
			"AND vr.situationId = :situationId " +
			"AND vr.evolutionId = :evolutionId")
	Integer getReportIdByPatientIdAndSituationIdAndEvolutionId(@Param("patientId") Integer patientId,
															   @Param("situationId") Short situationId,
															   @Param("evolutionId") Short evolutionId);

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.violencereport.domain.ViolenceEpisodeDetailBo(vr.episodeDate, vr.violenceTowardsUnderageTypeId, vr.schooled, vr.schoolLevelId, vr.riskLevelId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.id = :reportId")
	ViolenceEpisodeDetailBo getEpisodeDataByReportId(@Param("reportId") Integer reportId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW ar.lamansys.sgh.shared.domain.FilterOptionBo(i.id, i.name) " +
			"FROM ViolenceReport vr " +
			"JOIN Institution i ON (i.id = vr.institutionId) " +
			"WHERE vr.patientId = :patientId")
	List<FilterOptionBo> getInstitutionFilterByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW ar.lamansys.sgh.shared.domain.FilterOptionBo(vr.situationId, vr.situationId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId = :patientId")
	List<FilterOptionBo> getSituationFilterByPatientId(@Param("patientId") Integer patientId);

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT NEW net.pladema.violencereport.domain.ViolenceReportBo(vr.id, vr.situationId, vr.patientId) " +
			"FROM ViolenceReport vr " +
			"WHERE vr.patientId IN :patientIds")
	List<ViolenceReportBo> getAllPatientsSituationIds(@Param("patientIds") List<Integer> patientId);

	//The @Transactional label is not used because the only method that'll call this is already within a transaction.
	@Modifying
	@Query("UPDATE ViolenceReport vr SET vr.situationId = :situationId WHERE vr.id = :reportId")
	void updateViolenceReportSituationId(@Param("reportId") Integer reportId, @Param("situationId") Short situationId);

}
