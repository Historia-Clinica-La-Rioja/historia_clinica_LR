package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
