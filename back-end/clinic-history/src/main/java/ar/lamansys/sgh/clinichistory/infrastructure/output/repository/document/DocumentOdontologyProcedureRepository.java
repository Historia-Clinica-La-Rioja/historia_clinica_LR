package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentOdontologyProcedurePK;

@Repository
public interface DocumentOdontologyProcedureRepository extends JpaRepository<DocumentOdontologyProcedure, DocumentOdontologyProcedurePK> {

	@Transactional(readOnly = true)
	@Query("SELECT op, s, s1, s2 " +
			"FROM DocumentOdontologyProcedure dp " +
			"JOIN OdontologyProcedure op ON (dp.pk.odontologyProcedureId = op.id) " +
			"JOIN Snomed s ON (op.snomedId = s.id) " +
			"JOIN Snomed s1 ON (op.toothId = s1.id) " +
			"JOIN Snomed s2 ON (op.surfaceId = s2.id) " +
			"WHERE dp.pk.documentId = :documentId ")
    List<Object[]> getOdontologyProcedureFromDocument(@Param("documentId") Long documentId);

}
