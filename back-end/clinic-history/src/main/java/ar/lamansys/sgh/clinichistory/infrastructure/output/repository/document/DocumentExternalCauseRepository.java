package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCause;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCausePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentExternalCauseRepository extends JpaRepository<DocumentExternalCause, DocumentExternalCausePK> {
}
