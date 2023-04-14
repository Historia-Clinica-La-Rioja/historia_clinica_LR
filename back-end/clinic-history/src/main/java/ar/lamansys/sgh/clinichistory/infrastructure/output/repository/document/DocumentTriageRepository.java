package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentInmunizationPK;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentTriage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTriageRepository extends JpaRepository<DocumentTriage, DocumentTriagePK> {

}
