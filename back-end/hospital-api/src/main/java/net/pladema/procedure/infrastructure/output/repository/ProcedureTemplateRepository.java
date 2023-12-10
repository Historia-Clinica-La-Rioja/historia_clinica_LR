package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureTemplateRepository extends JpaRepository<ProcedureTemplate, Integer> {

}
