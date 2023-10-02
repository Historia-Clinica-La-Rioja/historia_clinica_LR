package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends SGXAuditableEntityJPARepository<ServiceRequest, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT sr.id " +
			"FROM ServiceRequest sr " +
			"WHERE sr.patientId IN :patientsIds")
	List<Integer> getServiceRequestIdsFromPatients(@Param("patientsIds") List<Integer> patientsIds);

	@Transactional
	@Query("SELECT sr.id " +
			"FROM ServiceRequest sr " +
			"WHERE sr.sourceId IN :sourceIds " +
			"AND sr.sourceTypeId = :typeId")
	List<Integer> getServiceRequestIdsFromIdSourceType(@Param("sourceIds") List<Integer> sourceIds, @Param("typeId") Short typeId);

	@Transactional(readOnly = true)
	@Query("SELECT sr.medicalCoverageId " +
			"FROM ServiceRequest sr " +
			"WHERE sr.id = :id")
	Optional<Integer> getMedicalCoverageId(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo(sr.id, " +
			"s.id, s.sctid, s.pt) " +
			"FROM ServiceRequest sr " +
			"JOIN Document d ON (sr.id = d.sourceId) " +
			"JOIN DocumentDiagnosticReport ddr ON (d.id = ddr.pk.documentId) " +
			"JOIN DiagnosticReport dr ON (ddr.pk.diagnosticReportId = dr.id) " +
			"JOIN Snomed s ON (dr.snomedId = s.id) " +
			"WHERE sr.id IN (:serviceRequestIds) " +
			"AND d.typeId = "+ DocumentType.ORDER)
	List<ServiceRequestProcedureInfoBo> getServiceRequestsProcedures(@Param("serviceRequestIds") List<Integer> serviceRequestIds);
}
