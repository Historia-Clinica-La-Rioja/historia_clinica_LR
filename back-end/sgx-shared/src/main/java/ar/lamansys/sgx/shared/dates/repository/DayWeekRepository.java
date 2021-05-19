package ar.lamansys.sgx.shared.dates.repository;

import ar.lamansys.sgx.shared.dates.repository.entity.DayWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayWeekRepository extends JpaRepository<DayWeek, Short> {
}
