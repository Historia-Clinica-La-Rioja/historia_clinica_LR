package ar.lamansys.sgh.publicapi.domain.prescription;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChangePrescriptionStateBo {
	String prescriptionId;
	String pharmacyName;
	String pharmacistName;
	String pharmacistRegistration;
	LocalDateTime changeDate;
	List<ChangePrescriptionStateMedicationBo> changePrescriptionStateLineMedicationList;
}
