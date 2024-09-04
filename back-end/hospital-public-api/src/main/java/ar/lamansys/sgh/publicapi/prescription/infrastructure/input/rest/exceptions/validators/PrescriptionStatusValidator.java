package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.validators;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;

import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionValidStatesEnum;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMedicationDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PrescriptionStatusValidator implements ConstraintValidator<ValidPrescriptionStatus, ChangePrescriptionStateDto> {

	@Override
	public void initialize(ValidPrescriptionStatus constraintAnnotation) {
		// nothing to do
	}

	@Override
	public boolean isValid(@Valid ChangePrescriptionStateDto changePrescriptionLineDto, ConstraintValidatorContext constraintValidatorContext) {

		boolean valid = true;

		if(changePrescriptionLineDto.getPharmacistName().isBlank() || changePrescriptionLineDto.getPharmacyName().isBlank()) {
			valid = false;
			buildResponse(constraintValidatorContext, "El nombre de farmacia o farmacéutico no pueden estar en blanco");
		}

		var emptyFieldsLines = emptyFieldsLines(changePrescriptionLineDto);
		if(!emptyFieldsLines.isEmpty()) {
			valid = false;
			for (Integer line : emptyFieldsLines) {
				buildResponse(constraintValidatorContext, "El renglón " + line + " tiene campos vacíos");
			}
		}

		var canceledLinesWithoutObservations = canceledLinesWithoutObservations(changePrescriptionLineDto);
		if(!canceledLinesWithoutObservations.isEmpty()) {
			valid = false;
			for(Integer line : canceledLinesWithoutObservations) {
				buildResponse(constraintValidatorContext, "El renglón " + line + " tiene estado CANCELADO y no tiene observaciones");
			}
		}

		var errorsSoldUnits = soldUnitsInvalid(changePrescriptionLineDto);
		if(!errorsSoldUnits.isEmpty()) {
			valid = false;
			for(Integer line : errorsSoldUnits) {
				buildResponse(constraintValidatorContext, "El renglón " + line + " no tiene unidades vendidas");
			}
		}

		var incorrectPayments = incorrectPayments(changePrescriptionLineDto);
		if(!incorrectPayments.isEmpty()) {
			valid = false;
			for(Integer line : incorrectPayments) {
				buildResponse(constraintValidatorContext, "En el renglón " + line + " la suma de pagos no coincide con el precio total");
			}
		}

		return valid;
	}

	private List<Integer> emptyFieldsLines(ChangePrescriptionStateDto changePrescriptionLineDto) {
		return changePrescriptionLineDto.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(changePrescriptionStateMedicationDto ->
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getBrand().isBlank() ||
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getCommercialPresentation().isBlank() ||
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getCommercialName().isBlank()
				)
				.map(ChangePrescriptionStateMedicationDto::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private List<Integer> canceledLinesWithoutObservations(ChangePrescriptionStateDto changePrescriptionLineDto) {
		return changePrescriptionLineDto.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(changePrescriptionStateMedicationDto ->
						changePrescriptionStateMedicationDto.getObservations() == null &&
						PrescriptionValidStatesEnum.CANCELADO_DISPENSA.equals(
								PrescriptionValidStatesEnum.map(changePrescriptionStateMedicationDto.getPrescriptionStateId())))
				.map(ChangePrescriptionStateMedicationDto::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private List<Integer> incorrectPayments(ChangePrescriptionStateDto changePrescriptionLineDto) {
		return changePrescriptionLineDto.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(changePrescriptionStateMedicationDto ->
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getAffiliatePayment() +
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getMedicalCoveragePayment() !=
						changePrescriptionStateMedicationDto.getDispensedMedicationDto().getPrice())
				.map(ChangePrescriptionStateMedicationDto::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private List<Integer> soldUnitsInvalid(ChangePrescriptionStateDto changePrescriptionLineDto) {
		return changePrescriptionLineDto.getChangePrescriptionStateLineMedicationList()
				.stream()
				.filter(changePrescriptionStateMedicationDto -> changePrescriptionStateMedicationDto.getDispensedMedicationDto().getSoldUnits() <= 0)
				.map(ChangePrescriptionStateMedicationDto::getPrescriptionLine)
				.collect(Collectors.toList());
	}

	private void buildResponse(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
				.addConstraintViolation();
	}
}
