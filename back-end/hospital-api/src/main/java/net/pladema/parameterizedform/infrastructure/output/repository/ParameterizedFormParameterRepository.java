package net.pladema.parameterizedform.infrastructure.output.repository;


import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedFormParameter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Repository
public interface ParameterizedFormParameterRepository extends JpaRepository<ParameterizedFormParameter, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pfp.orderNumber " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterizedFormId = :parameterizedFormId " +
			"ORDER BY pfp.orderNumber desc")
	List<Short> getLastFormsDescendingById(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query("SELECT pfp " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterizedFormId = :parameterizedFormId ")
	Page<ParameterizedFormParameter> findByParameterizedFormId(@Param("parameterizedFormId") Integer parameterizedFormId, Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT pfp " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterizedFormId = :parameterizedFormId  ")
	List<ParameterizedFormParameter> findAllByParameterizedFormId(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query(" SELECT COUNT(1) > 0 " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterizedFormId = :parameterizedFormId " +
			"AND pfp.parameterId = :parameterId ")
	Boolean alreadyExistsParameterizedFormParameters(@Param("parameterizedFormId") Integer parameterizedFormId,
													 @Param("parameterId") Integer parameterId);


	@Transactional(readOnly = true)
	@Query(value = "SELECT pfp " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterizedFormId = :parameterizedFormId " +
			"AND pfp.orderNumber = :orderNumber")
	List<ParameterizedFormParameter> findByParameterizedFormIdAndOrder(@Param("parameterizedFormId") Integer parameterizedFormId,
																		   @Param("orderNumber") Short orderNumber);

	@Transactional(readOnly = true)
	@Query(" SELECT COUNT(1) > 0 " +
			"FROM ParameterizedFormParameter pfp " +
			"WHERE pfp.parameterId = :parameterId ")
	Boolean isAssociatedAnActiveParameterizedForm(@Param("parameterId") Integer parameterId);

}
