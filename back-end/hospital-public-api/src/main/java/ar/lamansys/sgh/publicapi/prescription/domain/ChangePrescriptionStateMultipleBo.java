package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
public class ChangePrescriptionStateMultipleBo {

	private String prescriptionId;

	private String pharmacyName;

	private String pharmacistName;

	private String pharmacistRegistration;

	private LocalDateTime changeDate;

	private String identificationNumber;

	List<ChangePrescriptionStateMultipleMedicationBo> changePrescriptionStateLineMedicationList;

}
