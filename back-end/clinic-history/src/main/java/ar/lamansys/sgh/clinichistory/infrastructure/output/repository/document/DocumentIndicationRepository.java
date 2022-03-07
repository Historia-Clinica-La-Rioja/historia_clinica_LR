package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIndication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIndicationPK;

@Repository
public interface DocumentIndicationRepository extends JpaRepository<DocumentIndication, DocumentIndicationPK> {

}

