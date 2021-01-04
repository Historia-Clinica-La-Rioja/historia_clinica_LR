package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentDiagnosticReport;
import net.pladema.clinichistory.documents.repository.entity.DocumentDiagnosticReportPK;
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
