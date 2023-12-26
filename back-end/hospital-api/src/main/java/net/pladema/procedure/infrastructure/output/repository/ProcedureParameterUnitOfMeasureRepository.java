package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterUnitOfMeasure;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterUnitOfMeasurePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureParameterUnitOfMeasureRepository extends JpaRepository<ProcedureParameterUnitOfMeasure, ProcedureParameterUnitOfMeasurePK> {

}
