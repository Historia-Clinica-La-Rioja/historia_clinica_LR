package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalPrescriptionDto {
	String name;
	String lastName;
	String identificationType;
	String identificationNumber;
	String phoneNumber;
	String email;
	List<PrescriptionProfessionDto> professions;
	List<PrescriptionProfessionalRegistrationDto> registrations;
}
