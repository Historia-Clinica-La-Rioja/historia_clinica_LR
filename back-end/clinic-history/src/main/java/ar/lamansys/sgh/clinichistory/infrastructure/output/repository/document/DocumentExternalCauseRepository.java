package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ExternalCauseVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCause;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentExternalCausePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentExternalCauseRepository extends JpaRepository<DocumentExternalCause, DocumentExternalCausePK> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ExternalCauseVo( " +
			"ec.id, s, ec.externalCauseTypeId, ec.eventLocation) " +
			"FROM DocumentExternalCause dec " +
			"JOIN ExternalCause ec ON (dec.pk.externalCauseId = ec.id) " +
			"LEFT JOIN Snomed s ON (ec.snomedId = s.id) " +
			"WHERE dec.pk.documentId = :documentId ")
	ExternalCauseVo getExternalCauseFromDocument(@Param("documentId") Long documentId);

}
