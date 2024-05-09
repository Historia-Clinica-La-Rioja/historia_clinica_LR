package ar.lamansys.sgh.clinichistory.application.observation;

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
public class ObservationServiceImpl implements ObservationService {

	private final FhirObservationRepository fhirObservationRepository;
	private final FhirObservationGroupRepository fhirObservationGroupRepository;
	private final DiagnosticReportFhirObservationGroupRepository diagnosticReportObservationGroupRepository;
	private final FhirQuantityRepository fhirQuantityRepository;

	@Override
	public void save(FhirObservationGroupBo observationGroupBo) {
		FhirObservationGroup observationGroup = new FhirObservationGroup();
		observationGroup.setPatientId(observationGroupBo.getPatientId());
		observationGroup = fhirObservationGroupRepository.save(observationGroup);
		for (FhirObservationBo observation : observationGroupBo.getObservations()) {
			FhirObservation fhirObservation = new FhirObservation();
			fhirObservation.setFhirObservationGroupId(observationGroup.getId());
			fhirObservation.setLoincCode(observation.getLoincCode());
			if (observation.getValue() != null)
				fhirObservation.setValue(observation.getValue());
			if (observation.getQuantity() != null) {
				FhirQuantityBo quantity = observation.getQuantity();
				FhirQuantity fhirQuantity = new FhirQuantity();
				fhirQuantity.setValue(quantity.getValue());
				fhirQuantity.setUnit(quantity.getUnit());
				fhirObservation.setQuantityId(fhirQuantityRepository.save(fhirQuantity).getId());
			}
			fhirObservationRepository.save(fhirObservation);
		}
		diagnosticReportObservationGroupRepository.save(new DiagnosticReportFhirObservationGroup(observationGroup.getId(),observationGroupBo.getDiagnosticReportId()));

	}
}
