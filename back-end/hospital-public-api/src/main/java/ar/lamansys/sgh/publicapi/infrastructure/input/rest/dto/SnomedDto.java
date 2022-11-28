package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
public class SnomedDto implements Serializable {

	private String sctId;
	private String pt;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SnomedDto snomedDto = (SnomedDto) o;
		return sctId.equals(snomedDto.sctId) && pt.equals(snomedDto.pt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sctId, pt);
	}
}
