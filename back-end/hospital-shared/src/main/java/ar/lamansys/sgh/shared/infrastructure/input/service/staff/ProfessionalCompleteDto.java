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
public class ProfessionalCompleteDto {

	private Integer id;
	private Integer personId;
    private String firstName;
    private String lastName;
	private String nameSelfDetermination;
	private List<ProfessionCompleteDto> professions;

	public String getCompleteLicenseInfo() {
		return professions.stream()
				.flatMap(professionCompleteDto ->
						professionCompleteDto.getAllLicenses().stream()
								.map(LicenseNumberDto::getInfo))
				.collect(Collectors.joining(", "));
	}

	public List<LicenseNumberDto> getAllLicenses() {
		return professions.stream()
				.flatMap(professionCompleteDto -> professionCompleteDto.getAllLicenses().stream())
				.collect(Collectors.toList());
	}

	public String getCompleteName(String name){
		return String.format("%s %s", name, lastName);
	}
}
