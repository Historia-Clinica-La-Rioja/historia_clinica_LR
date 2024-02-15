package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureParameterTypeRepository extends JpaRepository<ProcedureParameterType, Short> {

}
