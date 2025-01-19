package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.domain.SnomedPracticeVo;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomed;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomedPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProcedureTemplateSnomedRepository extends JpaRepository<ProcedureTemplateSnomed, ProcedureTemplateSnomedPK> {

	@Transactional(readOnly = true)
	@Query("SELECT new net.pladema.procedure.domain.SnomedPracticeVo(s.id, s.sctid, s.pt) " +
			"FROM ProcedureTemplateSnomed pts " +
			"JOIN Snomed s ON pts.pk.snomedId = s.id " +
			"WHERE pts.pk.procedureTemplateId = :procedureTemplateId")
	List<SnomedPracticeVo> getAllPracticesByProcedureTemplateId(@Param("procedureTemplateId") Integer procedureTemplateId);

	@Transactional(readOnly = true)
	@Query("SELECT pt " +
			"FROM ProcedureTemplateSnomed pts " +
			"JOIN ProcedureTemplate pt ON pts.pk.procedureTemplateId = pt.id "+
			"WHERE pts.pk.snomedId = :snomedId")
	List<ProcedureTemplate> findProcedureTemplateBySnomedId(@Param("snomedId") Integer snomedId);

}
