package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class GenericMedicationBo {
	String name;
	String snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GenericMedicationBo that = (GenericMedicationBo) o;
		return Objects.equals(name, that.name) && Objects.equals(snomedId, that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, snomedId);
	}
}
