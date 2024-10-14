package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
public class SnomedCIE10Dto implements Serializable {

	private String sctId;
	private String pt;

	private String CIE10Id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnomedCIE10Dto snomedDto = (SnomedCIE10Dto) o;
		if (snomedDto.getCIE10Id() != null)
			return sctId.equals(snomedDto.sctId) && pt.equals(snomedDto.pt) && CIE10Id.equals(snomedDto.getCIE10Id());
		return sctId.equals(snomedDto.sctId) && pt.equals(snomedDto.pt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sctId, pt);
	}
}
