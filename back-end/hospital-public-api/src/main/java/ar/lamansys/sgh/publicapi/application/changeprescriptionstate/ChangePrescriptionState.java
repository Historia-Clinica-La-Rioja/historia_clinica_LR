package ar.lamansys.sgh.publicapi.application.changeprescriptionstate;

import ar.lamansys.sgh.publicapi.application.port.out.PrescriptionStorage;

import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateBo;

import ar.lamansys.sgh.publicapi.domain.prescription.ChangePrescriptionStateMedicationBo;

import ar.lamansys.sgh.publicapi.domain.prescription.DispensedMedicationBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ChangePrescriptionStateMedicationDto;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.DispensedMedicationDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ChangePrescriptionStateDto;

import java.util.stream.Collectors;

@Service
public class ChangePrescriptionState {
	private final Logger logger;
	private final PrescriptionStorage prescriptionStorage;

	public ChangePrescriptionState(PrescriptionStorage prescriptionStorage) {
		this.prescriptionStorage = prescriptionStorage;
		this.logger = LoggerFactory.getLogger(ChangePrescriptionState.class);
	}

	public void run(ChangePrescriptionStateDto changePrescriptionLineStateDto, String prescriptionId, String identificationNumber) {
		logger.debug("Input parameters -> changePrescriptionLineStateDto {}", changePrescriptionLineStateDto);
		prescriptionStorage.changePrescriptionState(mapTo(changePrescriptionLineStateDto), prescriptionId, identificationNumber);
		logger.debug("Output -> success");
	}

	private ChangePrescriptionStateBo mapTo(ChangePrescriptionStateDto changePrescriptionLineStateDto) {
		var returnBo = ChangePrescriptionStateBo.builder()
				.prescriptionId(changePrescriptionLineStateDto.getPrescriptionId())
				.changeDate(changePrescriptionLineStateDto.getChangeDate())
				.pharmacistName(changePrescriptionLineStateDto.getPharmacistName())
				.pharmacyName(changePrescriptionLineStateDto.getPharmacyName())
				.pharmacistRegistration(changePrescriptionLineStateDto.getPharmacistRegistration())
				.build();

		returnBo.setChangePrescriptionStateLineMedicationList(
				changePrescriptionLineStateDto.getChangePrescriptionStateLineMedicationList().stream()
						.map(this::mapTo)
						.collect(Collectors.toList())
		);
		return returnBo;
	}

	private ChangePrescriptionStateMedicationBo mapTo(ChangePrescriptionStateMedicationDto changePrescriptionStateMedicationDto) {

		return new ChangePrescriptionStateMedicationBo(
				changePrescriptionStateMedicationDto.getPrescriptionLine(),
				changePrescriptionStateMedicationDto.getPrescriptionStateId(),
				mapTo(changePrescriptionStateMedicationDto.getDispensedMedicationDto())
		);
	}

	private DispensedMedicationBo mapTo(DispensedMedicationDto dispensedMedicationDto) {
		return new DispensedMedicationBo(
				dispensedMedicationDto.getSnomedId(),
				dispensedMedicationDto.getCommercialName(),
				dispensedMedicationDto.getCommercialPresentation(),
				dispensedMedicationDto.getSoldUnits(),
				dispensedMedicationDto.getBrand(),
				dispensedMedicationDto.getPrice(),
				dispensedMedicationDto.getAffiliatePayment(),
				dispensedMedicationDto.getMedicalCoveragePayment());
	}
}
