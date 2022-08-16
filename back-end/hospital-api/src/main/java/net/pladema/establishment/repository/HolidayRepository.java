package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.Holiday;

import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends SGXAuditableEntityJPARepository<Holiday, Integer> {

}
