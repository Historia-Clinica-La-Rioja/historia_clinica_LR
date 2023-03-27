package net.pladema.person.repository;

import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.DuplicatePersonVo;
import net.pladema.person.repository.domain.PersonSearchResultVo;

import java.util.List;

public interface AuditPersonRepositorySearch {

	List<DuplicatePersonVo> getAllByFilter(AuditPatientSearch auditPatientSearch);
	List<PersonSearchResultVo> getPersonSearchResultByAttributes(DuplicatePersonVo duplicatePersonVo);
}
