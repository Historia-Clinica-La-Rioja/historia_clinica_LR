package net.pladema.parameter.infrastructure.output.repository;

import net.pladema.parameter.domain.ParameterTextOptionBo;
import net.pladema.parameter.infrastructure.output.repository.entity.ParameterTextOption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParameterTextOptionRepository extends JpaRepository<ParameterTextOption, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.parameter.domain.ParameterTextOptionBo(pto.id, pto.parameterId, pto.description) " +
			"FROM ParameterTextOption pto " +
			"WHERE pto.parameterId = :parameterId " +
			"AND pto.deleteable.deleted = false")
	List<ParameterTextOptionBo> getAllByParameterId(@Param("parameterId") Integer parameterId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ParameterTextOption pto " +
					"WHERE pto.parameterId = :parameterId")
	void deleteTextOptionFromParameterId(@Param("parameterId") Integer parameterId);

}
