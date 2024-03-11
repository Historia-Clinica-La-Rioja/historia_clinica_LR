package ar.lamansys.sgh.publicapi.prescription.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommercialMedicationBo {
	String name;
	String snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommercialMedicationBo that = (CommercialMedicationBo) o;
		return Objects.equals(name, that.name) && Objects.equals(snomedId, that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, snomedId);
	}
}
