package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class ProfessionalPrescriptionBo {
	String name;
	String lastName;
	String identificationType;
	String identificationNumber;
	String phoneNumber;
	String email;
	List<PrescriptionProfessionBo> professions;
	List<PrescriptionProfessionalRegistrationBo> registrations;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProfessionalPrescriptionBo)) return false;
		ProfessionalPrescriptionBo that = (ProfessionalPrescriptionBo) o;
		return Objects.equals(getName(), that.getName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getIdentificationType(), that.getIdentificationType()) && Objects.equals(getIdentificationNumber(), that.getIdentificationNumber()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getProfessions(), that.getProfessions()) && Objects.equals(getRegistrations(), that.getRegistrations());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getLastName(), getIdentificationType(), getIdentificationNumber(), getPhoneNumber(), getEmail(), getProfessions(), getRegistrations());
	}
}
