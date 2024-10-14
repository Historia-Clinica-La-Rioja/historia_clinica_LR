package ar.lamansys.sgh.publicapi.activities.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class SnomedCIE10Bo {
	private String sctId;
	private String pt;
	private String cie10Id;
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SnomedCIE10Bo)) return false;
		SnomedCIE10Bo that = (SnomedCIE10Bo) o;
		return Objects.equals(getSctId(), that.getSctId()) && Objects.equals(getPt(), that.getPt()) && Objects.equals(getCie10Id(), that.getCie10Id());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSctId(), getPt(), getCie10Id());
	}
}
