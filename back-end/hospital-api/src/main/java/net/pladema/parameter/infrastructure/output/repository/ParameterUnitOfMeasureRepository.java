package net.pladema.parameter.infrastructure.output.repository;

import net.pladema.parameter.domain.ParameterUnitOfMeasureBo;
import net.pladema.parameter.infrastructure.output.repository.entity.ParameterUnitOfMeasure;
import net.pladema.parameter.infrastructure.output.repository.entity.ParameterUnitOfMeasurePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ParameterUnitOfMeasureRepository extends JpaRepository<ParameterUnitOfMeasure, ParameterUnitOfMeasurePK> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT puom.pk.unitOfMeasureId " +
					"FROM ParameterUnitOfMeasure puom " +
					"WHERE puom.pk.parameterId = :parameterId ")
	Integer getUnitOfMeasureFromParameterId(@Param("parameterId") Integer parameterId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ParameterUnitOfMeasure puom " +
					"WHERE puom.pk.parameterId = :parameterId")
	void deleteUnitOfMeasureFromParameterId(@Param("parameterId") Integer parameterId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.parameter.domain.ParameterUnitOfMeasureBo(pum.pk.parameterId, pum.pk.unitOfMeasureId, um.code, um.description) " +
			"FROM ParameterUnitOfMeasure pum " +
			"JOIN UnitOfMeasure um ON (pum.pk.unitOfMeasureId = um.id) " +
			"WHERE pum.pk.parameterId = :parameterId")
	Optional<ParameterUnitOfMeasureBo> getByParameterId(@Param("parameterId") Integer parameterId);

}
