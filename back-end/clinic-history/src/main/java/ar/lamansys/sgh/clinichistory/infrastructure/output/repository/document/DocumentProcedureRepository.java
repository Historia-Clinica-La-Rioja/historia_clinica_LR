package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedurePK;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DocumentProcedureRepository extends JpaRepository<DocumentProcedure, DocumentProcedurePK> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo( " +
			"p.id, s, p.statusId, p.performedDate) " +
			"FROM DocumentProcedure dp " +
			"JOIN Procedure p ON (dp.pk.procedureId = p.id) " +
			"JOIN Snomed s ON (p.snomedId = s.id) " +
			"WHERE dp.pk.documentId = :documentId " +
			"AND p.statusId NOT IN ('"+ ProceduresStatus.ERROR+"')")
	List<ProcedureVo> getProcedureStateFromDocument(@Param("documentId") Long documentId);

}
