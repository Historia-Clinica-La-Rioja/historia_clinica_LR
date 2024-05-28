package ar.lamansys.sgh.clinichistory.infrastructure.output;

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
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FhirObservationStorageImpl implements FhirObservationStorage {
	private final FhirObservationRepository fhirObservationRepository;
	private final FhirObservationGroupRepository fhirObservationGroupRepository;
	private final DiagnosticReportFhirObservationGroupRepository diagnosticReportFhirObservationGroupRepository;
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
		diagnosticReportFhirObservationGroupRepository.save(
				new DiagnosticReportFhirObservationGroup(
						observationGroup.getId(),
						observationGroupBo.getDiagnosticReportId()
				)
		);
	}

	@Override
	public Optional<FhirObservationGroupBo> findByDiagnosticReportId(Integer diagnosticReportId) {
		return diagnosticReportFhirObservationGroupRepository
		.findByPk_diagnosticReportId(diagnosticReportId)
		.flatMap(diagReportGroupPk -> fhirObservationGroupRepository.findById(diagReportGroupPk.getPk().getFhirObservationGroupId()))
		.map(observationGroup -> {

			List<FhirObservation> observations = fhirObservationRepository.findByFhirObservationGroupId(observationGroup.getId());

			var quantityIds = observations.stream().map(FhirObservation::getQuantityId).collect(Collectors.toList());
			List<FhirQuantity> quantities = fhirQuantityRepository.findAllById(quantityIds);

			return new FhirObservationGroupBo(
				observationGroup.getId(),
				observationGroup.getPatientId(),
				diagnosticReportId,
				buildObservations(observations, quantities)
			);

		});
	}

	private List<FhirObservationBo> buildObservations(List<FhirObservation> observations, List<FhirQuantity> quantities) {
		return observations
		.stream()
		.map(observation ->
			new FhirObservationBo(
				observation.getId(),
				observation.getFhirObservationGroupId(),
				observation.getLoincCode(),
				observation.getValue(),
				findQuantity(observation.getQuantityId(), quantities)
			)
		)
		.collect(Collectors.toList());
	}

	private FhirQuantityBo findQuantity(Integer quantityId, List<FhirQuantity> quantities) {
		if (quantityId == null) return FhirQuantityBo.empty();
		return quantities.stream()
		.filter(x -> Objects.equals(x.getId(), quantityId))
		.findFirst()
		.map(x -> new FhirQuantityBo(x.getId(), x.getValue(), x.getUnit()))
		.orElseGet(() -> FhirQuantityBo.empty());
	}
}
