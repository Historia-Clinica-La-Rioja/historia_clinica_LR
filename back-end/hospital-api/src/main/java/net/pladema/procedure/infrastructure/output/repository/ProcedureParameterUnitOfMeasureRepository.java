package net.pladema.procedure.infrastructure.output.repository;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterUnitOfMeasure;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterUnitOfMeasurePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProcedureParameterUnitOfMeasureRepository extends JpaRepository<ProcedureParameterUnitOfMeasure, ProcedureParameterUnitOfMeasurePK> {

	@Transactional
	@Query(value = " SELECT ppuom.pk.unitOfMeasureId " +
			"FROM ProcedureParameterUnitOfMeasure ppuom " +
			"WHERE ppuom.pk.procedureParameterId = :procedureParameterId")
	List<Integer> getUnitOfMeasureFromProcedureParameterId(@Param("procedureParameterId") Integer procedureParameterId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ProcedureParameterUnitOfMeasure ppuom " +
			"WHERE ppuom.pk.procedureParameterId = :procedureParameterId")
	void deleteUnitOfMeasureFromProcedureParameterId(@Param("procedureParameterId") Integer procedureParameterId);

}
