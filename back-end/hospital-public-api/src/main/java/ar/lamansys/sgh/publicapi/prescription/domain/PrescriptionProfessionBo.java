package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrescriptionProfessionBo {
	String profession;
	String snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionProfessionBo that = (PrescriptionProfessionBo) o;
		return Objects.equals(profession, that.profession) && Objects.equals(snomedId, that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(profession, snomedId);
	}
}
