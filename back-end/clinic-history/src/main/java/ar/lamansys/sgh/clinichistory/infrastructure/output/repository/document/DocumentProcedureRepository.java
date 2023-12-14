package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedurePK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;

@Repository
public interface DocumentProcedureRepository extends JpaRepository<DocumentProcedure, DocumentProcedurePK> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo( " +
			"p.id, s, p.statusId, p.performedDate, p.isPrimary, n.description, p.procedureTypeId) " +
			"FROM DocumentProcedure dp " +
			"JOIN Procedure p ON (dp.pk.procedureId = p.id) " +
			"JOIN Snomed s ON (p.snomedId = s.id) " +
			"LEFT JOIN Note n ON (p.noteId = n.id) " +
			"WHERE dp.pk.documentId = :documentId " +
			"AND p.statusId NOT IN ('"+ ProceduresStatus.ERROR+"')")
	List<ProcedureVo> getProcedureStateFromDocument(@Param("documentId") Long documentId);

}
