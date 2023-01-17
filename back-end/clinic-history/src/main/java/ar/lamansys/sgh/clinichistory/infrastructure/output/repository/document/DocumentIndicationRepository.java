package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.Indication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIndication;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentIndicationPK;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentIndicationRepository extends JpaRepository<DocumentIndication, DocumentIndicationPK> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT n.description "
			+ "FROM DocumentIndication di "
			+ "JOIN Document doc ON di.pk.documentId = doc.id "
			+ "JOIN Note n ON n.id = doc.otherNoteId "
			+ "WHERE di.pk.indicationId = :indicationId ")
	String getNote(@Param("indicationId") Integer indicationId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT i "
			+ "FROM DocumentIndication di "
			+ "JOIN Indication i ON di.pk.indicationId = i.id "
			+ "WHERE di.pk.documentId IN :documentIds")
	List<Indication> getIndicationFromDocuments(@Param("documentIds") List<Long> documentIds);
}
