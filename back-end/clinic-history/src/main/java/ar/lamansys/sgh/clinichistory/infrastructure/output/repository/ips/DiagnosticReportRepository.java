package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

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

}
