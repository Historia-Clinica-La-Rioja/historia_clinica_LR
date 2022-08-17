package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.entity.Holiday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends SGXAuditableEntityJPARepository<Holiday, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT h " +
			"FROM Holiday h " +
			"WHERE h.date = :date " +
			"AND h.description = :description")
	Optional<Holiday> getByDateAndDescription(@Param("date")LocalDate date, @Param("description")String description);

}
