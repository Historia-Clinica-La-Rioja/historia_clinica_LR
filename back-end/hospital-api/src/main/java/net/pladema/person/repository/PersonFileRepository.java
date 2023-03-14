package net.pladema.person.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.person.repository.entity.PersonFile;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonFileRepository extends SGXAuditableEntityJPARepository<PersonFile, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT new ar.lamansys.sgh.shared.infrastructure.input.service.PersonFileDto(pf.id, pf.name) " +
			"FROM PersonFile pf " +
			"WHERE pf.personId = :personId " +
			"AND pf.deleteable.deleted = false")
	List<PersonFileDto> getFiles(@Param("personId") Integer personId);

}
