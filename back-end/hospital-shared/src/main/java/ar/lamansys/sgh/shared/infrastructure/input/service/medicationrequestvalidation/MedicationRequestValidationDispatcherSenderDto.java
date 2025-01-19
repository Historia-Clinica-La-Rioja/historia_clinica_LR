package ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation;

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
public class MedicationRequestValidationDispatcherSenderDto {

	private MedicationRequestValidationDispatcherPatientDto patient;

	private MedicationRequestValidationDispatcherProfessionalDto healthcareProfessional;

	private MedicationRequestValidationDispatcherInstitutionDto institution;

	private List<MedicationRequestValidationDispatcherMedicationDto> medications;

	private List<LocalDate> postdatedDates;

}
