package ar.lamansys.online.domain.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Builder
public class BookingInstitutionExtendedBo {
	private final Integer id;
	private final String description;
	private final String sisaCode;
	private final String dependency;
	private final String address;
	private final String city;
	private final String department;
	private final List<String> clinicalSpecialtiesNames;
	@Setter
	private List<String> aliases;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BookingInstitutionExtendedBo)) return false;
		BookingInstitutionExtendedBo that = (BookingInstitutionExtendedBo) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
