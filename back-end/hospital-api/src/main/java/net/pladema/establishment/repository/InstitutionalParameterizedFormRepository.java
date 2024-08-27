package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionalParameterizedFormRepository extends SGXAuditableEntityJPARepository<InstitutionalParameterizedForm, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT ipf " +
			"FROM InstitutionalParameterizedForm ipf " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId " +
			"AND ipf.institutionId = :institutionId")
	Optional<InstitutionalParameterizedForm> findByParameterizedFormIdAndInstitutionId(@Param("parameterizedFormId") Integer parameterizedFormId,
																					   @Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT ipf.institutionId " +
			"FROM InstitutionalParameterizedForm ipf " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId")
	Integer getInstitutionIdByParameterizedFormId(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query("SELECT ipf.isEnabled " +
			"FROM InstitutionalParameterizedForm ipf " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId")
	Boolean getEnabledByParameterizedFormId(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query("SELECT ipf " +
			"FROM InstitutionalParameterizedForm ipf " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId")
	List<InstitutionalParameterizedForm> getByParameterizedFormId(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query("SELECT ipf " +
			"FROM InstitutionalParameterizedForm ipf " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId " +
			"AND ipf.institutionId = :institutionId")
	Optional<InstitutionalParameterizedForm> getByParameterizedFormIdAndInstitutionId(@Param("parameterizedFormId") Integer parameterizedFormId,
																					  @Param("institutionId") Integer institutionId);

	@Transactional
	@Modifying
	@Query("UPDATE InstitutionalParameterizedForm ipf " +
			"SET ipf.isEnabled = false, ipf.updateable.updatedOn = CURRENT_TIMESTAMP " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId")
	void updateInstitutionalParameterizedFormEnabled(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(pf.id) > 0 THEN TRUE ELSE FALSE END) " +
					"FROM ParameterizedForm pf " +
					"JOIN InstitutionalParameterizedForm ipf ON pf.id = ipf.parameterizedFormId " +
					"WHERE pf.id != :formId " +
					"AND pf.name = :name " +
					"AND pf.isDomain = :isDomain " +
					"AND ipf.institutionId = :institutionId " +
					"AND ipf.isEnabled = true " +
					"AND pf.statusId = 2")
	Boolean existsParameterizedFormByNameAndInsitutionIdAndDomain(@Param("formId") Integer formId,
																	@Param("institutionId") Integer institutionId,
																	@Param("name") String name,
																	@Param("isDomain") Boolean isDomain);

}
