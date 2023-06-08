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
@Setter
public class BookingInstitutionExtendedBo {
    private final Integer id;
    private final String description;
	private final String sisaCode;
	private final String dependency;
	private final String address;

	private List<String> clinicalSpecialtiesNames;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookingInstitutionExtendedBo that = (BookingInstitutionExtendedBo) o;
		return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(sisaCode, that.sisaCode) && Objects.equals(dependency, that.dependency) && Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, description, sisaCode, dependency, address);
	}
}
