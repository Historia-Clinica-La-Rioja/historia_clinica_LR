package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class SuggestedCommercialMedicationBo {

	private String name;

	private String snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SuggestedCommercialMedicationBo that = (SuggestedCommercialMedicationBo) o;
		return Objects.equals(name, that.name) && Objects.equals(snomedId, that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, snomedId);
	}

}
