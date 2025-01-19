package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReportFhirObservationGroup;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReportFhirObservationGroupPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosticReportFhirObservationGroupRepository extends JpaRepository<DiagnosticReportFhirObservationGroup, DiagnosticReportFhirObservationGroupPK> {
	Optional<DiagnosticReportFhirObservationGroup> findByPk_diagnosticReportId(Integer diagnosticReportId);
}
