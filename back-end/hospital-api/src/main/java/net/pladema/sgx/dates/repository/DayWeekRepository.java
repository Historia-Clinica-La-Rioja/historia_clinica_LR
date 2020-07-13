package net.pladema.sgx.dates.repository;

import net.pladema.sgx.dates.repository.entity.DayWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayWeekRepository extends JpaRepository<DayWeek, Short> {
}
