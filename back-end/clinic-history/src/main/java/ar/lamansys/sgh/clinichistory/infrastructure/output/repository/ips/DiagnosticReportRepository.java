package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DiagnosticReportRepository extends SGXAuditableEntityJPARepository<DiagnosticReport, Integer>, SGXDocumentEntityRepository<DiagnosticReport> {

	@Override
	@Transactional(readOnly = true)
	@Query("SELECT dr " +
			"FROM DocumentDiagnosticReport ddr " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE ddr.pk.documentId IN :documentIds")
	List<DiagnosticReport> getEntitiesByDocuments(@Param("documentIds") List<Long> documentIds);

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo(" +
			"dr.id, d.sourceId, dr.healthConditionId, s_hc.sctid, s_hc.pt, hc.cie10Codes, s_dr.sctid, s_dr.pt) " +
			"FROM DiagnosticReport dr " +
			"JOIN DocumentDiagnosticReport ddr ON (dr.id = ddr.pk.diagnosticReportId) " +
			"JOIN Document d ON (ddr.pk.documentId = d.id) " +
			"JOIN Snomed s_dr ON (dr.snomedId = s_dr.id) " +
			"JOIN HealthCondition hc ON (dr.healthConditionId = hc.id) " +
			"JOIN Snomed s_hc ON (hc.snomedId = s_hc.id) " +
			"WHERE dr.id = :id ")
	Optional<DiagnosticReportBo> getDiagnosticReportById(@Param("id") Integer id);

	@Modifying
	@Query("UPDATE DiagnosticReport dr SET dr.statusId = :newStatus WHERE dr.id = :diagnosticReportId")
	void updateStatus(@Param("diagnosticReportId") Integer diagnosticReportId, @Param("newStatus") String partial);
}
