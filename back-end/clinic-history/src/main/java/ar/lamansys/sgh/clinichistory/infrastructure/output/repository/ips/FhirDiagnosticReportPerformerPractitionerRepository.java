package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirDiagnosticReportPerformerPractitioner;

@Repository
public interface FhirDiagnosticReportPerformerPractitionerRepository extends JpaRepository<FhirDiagnosticReportPerformerPractitioner, Integer> {
}
