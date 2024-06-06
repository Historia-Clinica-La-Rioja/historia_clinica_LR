package net.pladema.clinichistory.requests.servicerequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
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
			"s.id, s.sctid, s.pt, dr.id, dr.statusId, src.description) " +
			"FROM ServiceRequest sr " +
			"JOIN ServiceRequestCategory src ON (sr.categoryId = src.id) " +
			"JOIN Document d ON (sr.id = d.sourceId) " +
			"JOIN DocumentDiagnosticReport ddr ON (d.id = ddr.pk.documentId) " +
			"JOIN DiagnosticReport dr ON (ddr.pk.diagnosticReportId = dr.id) " +
			"JOIN Snomed s ON (dr.snomedId = s.id) " +
			"WHERE sr.id IN (:serviceRequestIds) " +
			"AND d.typeId = "+ DocumentType.ORDER)
	List<ServiceRequestProcedureInfoBo> getServiceRequestsProcedures(@Param("serviceRequestIds") List<Integer> serviceRequestIds);

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo(sr.id, " +
			"s.id, s.sctid, s.pt, dr.id, dr.statusId, src.description) " +
			"FROM ServiceRequest sr " +
			"JOIN ServiceRequestCategory src ON (sr.categoryId = src.id) " +
			"JOIN Document d ON (sr.id = d.sourceId) " +
			"JOIN DocumentDiagnosticReport ddr ON (d.id = ddr.pk.documentId) " +
			"JOIN DiagnosticReport dr ON (ddr.pk.diagnosticReportId = dr.id) " +
			"JOIN Snomed s ON (dr.snomedId = s.id) " +
			"WHERE sr.id = :serviceRequestId " +
			"AND d.typeId = " + DocumentType.ORDER + " " +
			"AND NOT EXISTS " +
			"	(SELECT 1 " +
			"	FROM DocumentDiagnosticReport ddr2 " +
			"	JOIN DiagnosticReport dr2 ON (ddr2.pk.diagnosticReportId = dr2.id) " +
			"	WHERE ddr2.pk.documentId = d.id AND dr2.statusId <> '" + DiagnosticReportStatus.REGISTERED + "')")
	List<ServiceRequestProcedureInfoBo> getActiveServiceRequestProcedures(@Param("serviceRequestId") Integer serviceRequestId);

}
