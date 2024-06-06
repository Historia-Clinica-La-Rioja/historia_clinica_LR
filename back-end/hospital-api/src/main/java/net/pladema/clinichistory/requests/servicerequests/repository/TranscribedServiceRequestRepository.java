package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import java.util.List;
import java.util.Optional;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.TranscribedServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TranscribedServiceRequestRepository extends JpaRepository<TranscribedServiceRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo(" +
			"dr.id, tsr.id, dr.healthConditionId, s_hc.sctid, s_hc.pt, hc.cie10Codes, s_dr.sctid, s_dr.pt) " +
			"FROM TranscribedServiceRequest tsr " +
			"JOIN TranscribedServiceRequestDiagnosticReport tsrdr ON (tsr.id = tsrdr.pk.transcribedServiceRequestId) " +
			"JOIN DiagnosticReport dr ON (tsrdr.pk.diagnosticReportId = dr.id) " +
			"JOIN Snomed s_dr ON (dr.snomedId = s_dr.id) " +
			"JOIN HealthCondition hc ON (dr.healthConditionId = hc.id) " +
			"JOIN Snomed s_hc ON (hc.snomedId = s_hc.id) " +
			"WHERE tsr.id = :orderId ")
	List<DiagnosticReportBo> getDiagnosticReports(@Param("orderId") Integer orderId);

	@Transactional(readOnly = true)
	@Query("SELECT tsr.healthcareProfessionalName " +
			"FROM TranscribedServiceRequest tsr " +
			"WHERE tsr.id = :orderId ")
	Optional<String> getHealthcareProfessionalName(@Param("orderId") Integer orderId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo(" +
			"tsr.id, tsr.patientId, tsr.healthcareProfessionalName, tsr.institutionName, tsr.creationDate, " +
			"tsr.observations) " +
			"FROM TranscribedServiceRequest tsr " +
			"WHERE tsr.id = :orderId ")
	Optional<TranscribedServiceRequestBo> getTranscribedServiceRequest(@Param("orderId") Integer orderId);

}
