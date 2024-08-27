package net.pladema.parameterizedform.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.parameterizedform.domain.ParameterizedFormBo;
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
			"AND pf.name = :name " +
			"AND pf.isDomain = true")
	Boolean existsDomainParameterizedFormByName(@Param("formId") Integer formId,
												@Param("name") String name);


	@Transactional(readOnly = true)
	@Query("SELECT pf " +
			"FROM ParameterizedForm pf " +
			"WHERE (LOWER(pf.name) LIKE LOWER(concat('%', :name, '%'))) " +
			"AND pf.statusId IN :statusIds " +
			"AND pf.isDomain = :isDomain")
	Page<ParameterizedForm> getFormsByFilters(@Param("statusIds") List<Short> statusIds,
											  @Param("isDomain") Boolean isDomain,
											  @Param("name") String name,
											  Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT pf " +
			"FROM ParameterizedForm pf " +
			"WHERE pf.statusId IN :statusIds " +
			"AND pf.isDomain = :isDomain")
	Page<ParameterizedForm> getFormsByStatusAndDomain(@Param("statusIds") List<Short> statusIds,
													  @Param("isDomain") Boolean isDomain,
													  Pageable pageable);


	@Transactional(readOnly = true)
	@Query(value = "SELECT pf.statusId " +
			"FROM ParameterizedForm pf " +
			"WHERE pf.id = :formId")
	Optional<Short> findStatusById(@Param("formId") Integer formId);

	@Transactional
	@Modifying
	@Query("UPDATE InstitutionalParameterizedForm ipf " +
			"SET ipf.isEnabled = false, ipf.updateable.updatedOn = CURRENT_TIMESTAMP " +
			"WHERE ipf.parameterizedFormId = :parameterizedFormId")
	void updateInstitutionalParameterizedFormEnabled(@Param("parameterizedFormId") Integer parameterizedFormId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.parameterizedform.domain.ParameterizedFormBo(pf.id, pf.name, pf.statusId, " +
			"pf.outpatientEnabled, pf.internmentEnabled, pf.emergencyCareEnabled, pf.isDomain, ipf.institutionId, ipf.isEnabled) " +
			"FROM ParameterizedForm pf " +
			"JOIN InstitutionalParameterizedForm ipf ON (pf.id = ipf.parameterizedFormId) " +
			"WHERE ipf.institutionId = :institutionId " +
			"AND pf.statusId = 2 " +
			"AND ipf.isEnabled IS TRUE " +
			"AND pf.deleteable.deleted IS FALSE " +
			"AND ipf.deleteable.deleted IS FALSE ")
	List<ParameterizedFormBo> getActiveFormsByInstitution(@Param("institutionId") Integer institutionId);

}
