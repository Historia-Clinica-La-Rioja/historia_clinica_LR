package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomed;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplateSnomedPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureTemplateSnomedRepository extends JpaRepository<ProcedureTemplateSnomed, ProcedureTemplateSnomedPK> {
}
