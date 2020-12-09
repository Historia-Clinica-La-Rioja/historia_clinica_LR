package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.DocumentDiagnosticReport;
import net.pladema.clinichistory.documents.repository.entity.DocumentDiagnosticReportPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDiagnosticReportRepository extends JpaRepository<DocumentDiagnosticReport, DocumentDiagnosticReportPK> {

}
