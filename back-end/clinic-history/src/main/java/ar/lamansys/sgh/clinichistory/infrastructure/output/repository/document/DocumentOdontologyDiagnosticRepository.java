package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnostic;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyDiagnosticPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentOdontologyDiagnosticRepository extends JpaRepository<DocumentOdontologyDiagnostic, DocumentOdontologyDiagnosticPK> {
}
