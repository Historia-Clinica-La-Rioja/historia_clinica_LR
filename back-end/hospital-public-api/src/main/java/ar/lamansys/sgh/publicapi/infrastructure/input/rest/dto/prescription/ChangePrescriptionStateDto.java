package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class ChangePrescriptionStateDto {
	String prescriptionId;
	String pharmacyName;
	String pharmacistName;
	String pharmacistRegistration;
	LocalDateTime changeDate;
	List<ChangePrescriptionStateMedicationDto> changePrescriptionStateLineMedicationList;

}
