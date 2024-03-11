package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class PrescriptionProblemBo {
	String pt;
	String snomedId;
	String problemType;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PrescriptionProblemBo that = (PrescriptionProblemBo) o;
		return Objects.equals(pt, that.pt) && Objects.equals(snomedId, that.snomedId) && Objects.equals(problemType, that.problemType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pt, snomedId, problemType);
	}
}
