package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import java.util.ArrayList;
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
public class ProfessionalCompleteDto {

	private Integer personId;
    private String firstName;
    private String lastName;
	private String nameSelfDetermination;
	private List<ProfessionCompleteDto> professions;

	public String getCompleteLicenseInfo() {
		StringBuilder result = new StringBuilder();
		List<LicenseNumberDto> licenses = new ArrayList<>();
		professions.forEach(professionCompleteDto -> {
			licenses.addAll(professionCompleteDto.getLicenses());
		});
		return licenses.stream()
				.map(LicenseNumberDto::getInfo)
				.collect(Collectors.joining(","));
	}
}
