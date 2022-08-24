package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import java.util.List;
import java.util.stream.Collectors;

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
}
