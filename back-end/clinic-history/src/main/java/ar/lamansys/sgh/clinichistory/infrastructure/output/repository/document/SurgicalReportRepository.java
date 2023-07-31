package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.surgicalreport.SurgicalReport;

@Repository
public interface SurgicalReportRepository extends JpaRepository<SurgicalReport, Integer> {

}
