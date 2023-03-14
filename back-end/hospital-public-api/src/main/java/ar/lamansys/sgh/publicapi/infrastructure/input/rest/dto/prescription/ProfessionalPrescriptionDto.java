package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionalRegistrationBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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
	List<PrescriptionProfessionDto> specialties;
	List<PrescriptionProfessionalRegistrationDto> registrations;
}
