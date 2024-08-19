package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.application.ports.FhirObservationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirObservationGroupBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FhirQuantityBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportFhirObservationGroupRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FhirObservationGroupRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FhirObservationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FhirQuantityRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReportFhirObservationGroup;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirObservation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirObservationGroup;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirQuantity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SaveFhirObservationServiceImpl implements SaveFhirObservationService {
	private final FhirObservationStorage fhirObservationStorage;

	@Override
	public void save(FhirObservationGroupBo observationGroupBo) {
		fhirObservationStorage.save(observationGroupBo);
	}
}
