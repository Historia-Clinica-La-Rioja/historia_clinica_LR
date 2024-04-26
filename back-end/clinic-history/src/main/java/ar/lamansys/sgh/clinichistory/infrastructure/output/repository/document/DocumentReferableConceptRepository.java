package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConcept;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConceptPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentReferableConceptRepository extends JpaRepository<DocumentReferableConcept, DocumentReferableConceptPK> {
}
