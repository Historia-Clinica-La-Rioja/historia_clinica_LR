package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
public class BookingProfessionalBo {
	private final Integer id;
	private final String name;
	private final Boolean coverage;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BookingProfessionalBo)) return false;
		BookingProfessionalBo that = (BookingProfessionalBo) o;
		return getId().equals(that.getId()) && getCoverage().equals(that.getCoverage());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
