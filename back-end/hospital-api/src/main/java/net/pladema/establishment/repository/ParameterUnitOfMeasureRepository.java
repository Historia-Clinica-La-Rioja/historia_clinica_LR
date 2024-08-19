package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.ParameterUnitOfMeasure;
import net.pladema.establishment.repository.entity.ParameterUnitOfMeasurePK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
