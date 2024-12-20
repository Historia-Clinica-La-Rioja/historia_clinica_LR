package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionCompleteDto {

	private Integer id;

	private Integer professionId;

	private String description;

	private List<LicenseNumberDto> licenses;

	private List<ProfessionSpecialtyDto> specialties;


	public List<LicenseNumberDto> getAllLicenses() {
		var result = licenses;
		result.addAll(specialties.stream()
				.flatMap(ps -> ps.getLicenses().stream())
				.collect(Collectors.toList()));
		return result;
	}

	@JsonIgnore
	public boolean hasMN() {
		return (hasLicenses() && licenses.stream().anyMatch(LicenseNumberDto::hasMN))
				|| (hasSpecialties() && specialties.stream().anyMatch(ProfessionSpecialtyDto::hasMN));
	}

	@JsonIgnore
	public boolean hasMP() {
		return (hasLicenses() && licenses.stream().anyMatch(LicenseNumberDto::hasMP))
				|| (hasSpecialties() && specialties.stream().anyMatch(ProfessionSpecialtyDto::hasMP));
	}

	@JsonIgnore
	public boolean hasLicenses() {
		return licenses != null && !licenses.isEmpty();
	}

	@JsonIgnore
	public boolean hasSpecialties() {
		return (specialties != null && !specialties.isEmpty());
	}
}
