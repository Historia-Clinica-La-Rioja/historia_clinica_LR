package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEventPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentObstetricEventRepository extends JpaRepository<DocumentObstetricEvent, DocumentObstetricEventPK> {
}
