package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReportPK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DocumentDiagnosticReportRepository extends JpaRepository<DocumentDiagnosticReport, DocumentDiagnosticReportPK> {

    @Transactional(readOnly = true)
    @Query("SELECT ddr " +
            "FROM DocumentDiagnosticReport ddr " +
            "WHERE ddr.pk.diagnosticReportId = :drid ")
    DocumentDiagnosticReport findByDiagnosticReportId(@Param("drid")  Integer drid);

	@Transactional(readOnly = true)
	@Query("SELECT dr " +
			"FROM DocumentDiagnosticReport ddr " +
			"JOIN DiagnosticReport dr ON ddr.pk.diagnosticReportId = dr.id " +
			"WHERE ddr.pk.documentId IN :documentIds")
	List<DiagnosticReport> getDiagnosticReportFromDocuments(@Param("documentIds") List<Long> documentIds);
}
