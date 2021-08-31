package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentStatusRepository extends JpaRepository<DocumentStatus, String> {

}
