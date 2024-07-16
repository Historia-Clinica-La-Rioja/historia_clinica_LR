package net.pladema.parameterizedform.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParameterizedFormRepository extends SGXAuditableEntityJPARepository<ParameterizedForm, Integer> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE ParameterizedForm pf " +
			"SET pf.statusId = :statusId " +
			"WHERE pf.id = :formId")
	void updateStatusByFormId(@Param("formId") Integer formId,
							  @Param("statusId") Short statusId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(pf.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ParameterizedForm pf " +
			"WHERE pf.statusId = 2 " +
			"AND pf.id != :formId " +
			"AND pf.name = :name")
	Boolean existsFormByName(@Param("formId") Integer formId,
							 @Param("name") String name);


	@Transactional(readOnly = true)
	@Query("SELECT pf " +
			"FROM ParameterizedForm pf " +
			"WHERE (LOWER(pf.name) LIKE LOWER(concat('%', :name, '%'))) " +
			"AND pf.statusId IN :statusIds")
	Page<ParameterizedForm> getFormsByNameAndStatus(@Param("statusIds") List<Short> statusIds,
													@Param("name") String name,
													Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT pf " +
			"FROM ParameterizedForm pf " +
			"WHERE pf.statusId IN :statusIds")
	Page<ParameterizedForm> getFormsByStatus(@Param("statusIds") List<Short> statusIds,
											 Pageable pageable);


	@Transactional(readOnly = true)
	@Query(value = "SELECT pf.statusId " +
			"FROM ParameterizedForm pf " +
			"WHERE pf.id = :formId")
	Optional<Short> findStatusById(@Param("formId") Integer formId);

}
