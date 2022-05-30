package ar.lamansys.online.domain.specialty;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class BookingSpecialtyBo {
    private final Integer id;
    private final String description;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BookingSpecialtyBo)) return false;
		BookingSpecialtyBo that = (BookingSpecialtyBo) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}