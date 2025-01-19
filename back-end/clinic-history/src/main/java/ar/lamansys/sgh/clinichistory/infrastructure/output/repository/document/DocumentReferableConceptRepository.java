package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.domain.ReferableConceptVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConcept;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReferableConceptPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentReferableConceptRepository extends JpaRepository<DocumentReferableConcept, DocumentReferableConceptPK> {

	@Transactional(readOnly = true)
	@Query("SELECT drc.isReferred " +
			"FROM DocumentReferableConcept drc " +
			"WHERE drc.pk.documentId = :documentId " +
			"AND drc.pk.referableConceptId = :referableConceptId")
	Boolean isReferredIdByDocumentAndConceptId(@Param("documentId") Long documentId, @Param("referableConceptId") Short referableConceptId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.domain.ReferableConceptVo(drc.pk.referableConceptId, drc.isReferred) " +
			"FROM DocumentReferableConcept drc " +
			"WHERE drc.pk.documentId = :documentId")
    List<ReferableConceptVo> fetchDocumentReferredConcepts(@Param("documentId") Long documentId);

}
