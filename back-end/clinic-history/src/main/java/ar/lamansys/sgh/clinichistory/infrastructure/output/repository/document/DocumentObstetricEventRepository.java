package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentObstetricEventPK;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Newborn;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObstetricEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface DocumentObstetricEventRepository extends JpaRepository<DocumentObstetricEvent, DocumentObstetricEventPK> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT oe " +
			"FROM DocumentObstetricEvent doe " +
			"JOIN ObstetricEvent oe ON (doe.pk.obstetricEventId = oe.id) " +
			"WHERE doe.pk.documentId = (:documentId) ")
	ObstetricEvent getObstetricEventFromDocument(@Param("documentId") Long documentId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT n " +
			"FROM DocumentObstetricEvent doe " +
			"JOIN ObstetricEvent oe ON (doe.pk.obstetricEventId = oe.id) " +
			"JOIN Newborn n ON (n.obstetricEventId = oe.id) " +
			"WHERE doe.pk.documentId = (:documentId) ")
	List<Newborn> getObstetricEventNewbornsFromDocument(@Param("documentId") Long documentId);

}
