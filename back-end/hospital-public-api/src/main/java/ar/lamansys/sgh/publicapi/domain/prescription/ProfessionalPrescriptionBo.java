package ar.lamansys.sgh.publicapi.domain.prescription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProfessionalPrescriptionBo {
	String name;
	String lastName;
	String identificationType;
	String identificationNumber;
	String phoneNumber;
	String email;
	List<PrescriptionProfessionBo> specialties;
	List<PrescriptionProfessionalRegistrationBo> registrations;
}
