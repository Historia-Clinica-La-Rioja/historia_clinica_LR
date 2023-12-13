package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
public class PrescriptionSpecialtyBo {
	String specialty;
	String snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionSpecialtyBo that = (PrescriptionSpecialtyBo) o;
		return Objects.equals(specialty, that.specialty) && Objects.equals(snomedId, that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(specialty, snomedId);
	}
}
