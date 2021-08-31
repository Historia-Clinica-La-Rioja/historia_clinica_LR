package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedurePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentOdontologyProcedureRepository extends JpaRepository<DocumentOdontologyProcedure, DocumentOdontologyProcedurePK> {
}
