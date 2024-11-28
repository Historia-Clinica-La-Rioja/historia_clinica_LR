package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestValidationDispatcherSenderBo {

	private MedicationRequestValidationDispatcherPatientBo patient;

	private MedicationRequestValidationDispatcherProfessionalBo healthcareProfessional;

	private MedicationRequestValidationDispatcherInstitutionBo institution;

	private List<MedicationRequestValidationDispatcherMedicationBo> medications;

	private List<LocalDate> postdatedDates;

}
