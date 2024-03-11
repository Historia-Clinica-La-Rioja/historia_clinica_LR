package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrescriptionProfessionalRegistrationBo {
	String registrationNumber;
	String registrationType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionProfessionalRegistrationBo that = (PrescriptionProfessionalRegistrationBo) o;
		return Objects.equals(registrationNumber, that.registrationNumber) && Objects.equals(registrationType, that.registrationType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(registrationNumber, registrationType);
	}
}
