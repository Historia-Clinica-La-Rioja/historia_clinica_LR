package net.pladema.clinichistory.requests.servicerequests.repository;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TranscribedServiceRequestRepository extends JpaRepository<TranscribedServiceRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT tsr.studyId " +
			"FROM TranscribedServiceRequest tsr " +
			"WHERE tsr.id = :orderId ")
	Integer getStudyIdByOrderId(@Param("orderId") Integer orderId);

	@Transactional(readOnly = true)
	@Query("SELECT tsr.healthcareProfessionalName " +
			"FROM TranscribedServiceRequest tsr " +
			"WHERE tsr.id = :orderId ")
	Optional<String> getHealthcareProfessionalName(@Param("orderId") Integer orderId);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo(" +
			"tsr.id, tsr.patientId, " +
			"s_problem.sctid, s_problem.pt, hc.cie10Codes, " +
			"s_study.sctid, s_study.pt, " +
			"tsr.healthcareProfessionalName, tsr.institutionName, tsr.creationDate) " +
			"FROM TranscribedServiceRequest tsr " +
			"JOIN DiagnosticReport dr ON (tsr.studyId = dr.id) " +
			"JOIN Snomed s_study ON (dr.snomedId = s_study.id) " +
			"JOIN HealthCondition hc ON (dr.healthConditionId = hc.id) " +
			"JOIN Snomed s_problem ON (hc.snomedId = s_problem.id) " +
			"WHERE tsr.id = :id ")
	Optional<TranscribedServiceRequestBo> get(@Param("id") Integer id);

}
