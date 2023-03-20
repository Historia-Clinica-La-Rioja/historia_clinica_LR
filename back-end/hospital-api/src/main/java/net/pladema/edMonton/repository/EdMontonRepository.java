package net.pladema.edMonton.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.edMonton.repository.domain.QuestionnaireResponse;

public interface EdMontonRepository extends SGXAuditableEntityJPARepository<QuestionnaireResponse, Integer> {

}
