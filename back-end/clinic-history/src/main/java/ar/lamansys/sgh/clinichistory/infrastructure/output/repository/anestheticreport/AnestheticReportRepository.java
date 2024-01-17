package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnestheticReportRepository extends JpaRepository<AnestheticReport, Integer> {
}
