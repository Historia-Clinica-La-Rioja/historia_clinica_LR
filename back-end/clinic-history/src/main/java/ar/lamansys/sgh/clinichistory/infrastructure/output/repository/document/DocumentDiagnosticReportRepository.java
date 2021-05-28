package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDiagnosticReportPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface DocumentDiagnosticReportRepository extends JpaRepository<DocumentDiagnosticReport, DocumentDiagnosticReportPK> {

    @Transactional(readOnly = true)
    @Query("SELECT ddr " +
            "FROM DocumentDiagnosticReport ddr " +
            "WHERE ddr.pk.diagnosticReportId = :drid ")
    DocumentDiagnosticReport findByDiagnosticReportId(@Param("drid")  Integer drid);
}
