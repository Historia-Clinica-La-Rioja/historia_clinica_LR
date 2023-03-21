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
	private String prescriptionId;
	private String pharmacyName;
	private String pharmacistName;
	private String pharmacistRegistration;
	private LocalDateTime changeDate;
	List<ChangePrescriptionStateMedicationBo> changePrescriptionStateLineMedicationList;
}
