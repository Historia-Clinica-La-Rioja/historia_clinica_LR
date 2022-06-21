package ar.lamansys.online.domain.specialty;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class PracticeBo {
	private final Integer id;
	private final String description;
	private final Boolean coverage;
	private final String coverageText;
	private final Integer snomedId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PracticeBo)) return false;
		PracticeBo that = (PracticeBo) o;
		return getId().equals(that.getId()) && getCoverage().equals(that.getCoverage());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
