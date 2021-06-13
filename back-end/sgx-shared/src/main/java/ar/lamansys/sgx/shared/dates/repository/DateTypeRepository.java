package ar.lamansys.sgx.shared.dates.repository;

import ar.lamansys.sgx.shared.dates.repository.entity.DateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DateTypeRepository extends JpaRepository<DateType, LocalDate> {
}
