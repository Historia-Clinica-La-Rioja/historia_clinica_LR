package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.application.ports.FhirObservationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FetchFhirObservationServiceImpl implements FetchFhirObservationService {

	private final FhirObservationStorage fhirObservationStorage;
	@Override
	public Optional<FhirObservationGroupBo> run(Integer diagnosticReportId) {
		return fhirObservationStorage.findByDiagnosticReportId(diagnosticReportId);
	}
}
