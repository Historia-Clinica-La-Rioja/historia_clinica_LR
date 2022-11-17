package net.pladema.establishment.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.establishment.repository.domain.HolidayVo;
import net.pladema.establishment.repository.entity.Holiday;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends SGXAuditableEntityJPARepository<Holiday, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT h " +
			"FROM Holiday h " +
			"WHERE h.date = :date " +
			"AND h.description = :description")
	Optional<Holiday> getByDateAndDescription(@Param("date")LocalDate date, @Param("description")String description);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.establishment.repository.domain.HolidayVo( " +
			"h.date, h.description) " +
			"FROM Holiday h " +
			"WHERE h.date BETWEEN :startDate AND :endDate")
	List<HolidayVo> getHolidays(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
