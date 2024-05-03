package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstate;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapperImpl;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.DispensedMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.DispensedMedicationDto;

@Service
@AllArgsConstructor
@Slf4j
public class ChangePrescriptionState {
	private final PrescriptionStorage prescriptionStorage;
	private final LocalDateMapper localDateMapper;

	public void run(ChangePrescriptionStateDto changePrescriptionLineStateDto, PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) throws PrescriptionNotFoundException {
		log.debug("Input parameters -> changePrescriptionLineStateDto {}", changePrescriptionLineStateDto);
		prescriptionStorage.changePrescriptionState(mapTo(changePrescriptionLineStateDto), prescriptionIdentifier, identificationNumber);
		log.debug("Output -> success");
	}

	private ChangePrescriptionStateBo mapTo(ChangePrescriptionStateDto changePrescriptionStateDto) {
		var returnBo = ChangePrescriptionStateBo.builder()
				.prescriptionId(changePrescriptionStateDto.getPrescriptionId())
				.changeDate(localDateMapper.fromStringToLocalDateTime(changePrescriptionStateDto.getChangeDate()))
				.pharmacistName(changePrescriptionStateDto.getPharmacistName())
				.pharmacyName(changePrescriptionStateDto.getPharmacyName())
				.pharmacistRegistration(changePrescriptionStateDto.getPharmacistRegistration())
				.build();

		returnBo.setChangePrescriptionStateLineMedicationList(
				changePrescriptionStateDto.getChangePrescriptionStateLineMedicationList().stream()
						.map(ob -> mapTo(ob, changePrescriptionStateDto))
						.collect(Collectors.toList())
		);
		return returnBo;
	}

	private ChangePrescriptionStateMedicationBo mapTo(ChangePrescriptionStateMedicationDto changePrescriptionStateMedicationDto,
													  ChangePrescriptionStateDto changePrescriptionStateDto) {

		return new ChangePrescriptionStateMedicationBo(
				changePrescriptionStateMedicationDto.getPrescriptionLine(),
				changePrescriptionStateMedicationDto.getPrescriptionStateId(),
				mapTo(changePrescriptionStateMedicationDto.getDispensedMedicationDto(), changePrescriptionStateDto),
				changePrescriptionStateMedicationDto.getObservations(),
				changePrescriptionStateDto.getPharmacyName(),
				changePrescriptionStateDto.getPharmacistName()
		);
	}

	private DispensedMedicationBo mapTo(DispensedMedicationDto dispensedMedicationDto, ChangePrescriptionStateDto changePrescriptionStateDto) {
		return new DispensedMedicationBo(
				dispensedMedicationDto.getSnomedId(),
				dispensedMedicationDto.getCommercialName(),
				dispensedMedicationDto.getCommercialPresentation(),
				dispensedMedicationDto.getSoldUnits(),
				dispensedMedicationDto.getBrand(),
				dispensedMedicationDto.getPrice(),
				dispensedMedicationDto.getAffiliatePayment(),
				dispensedMedicationDto.getMedicalCoveragePayment(),
				changePrescriptionStateDto.getPharmacyName(),
				changePrescriptionStateDto.getPharmacistName(),
				dispensedMedicationDto.getObservations());
	}
}
