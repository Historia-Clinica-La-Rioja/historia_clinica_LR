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
public class ProfessionalCompleteDto {

	private Integer id;
	private Integer personId;
    private String firstName;
	private String middleNames;
    private String lastName;
	private String nameSelfDetermination;
	private List<ProfessionCompleteDto> professions;
	private String otherLastNames;

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
		if (otherLastNames != null)
			return String.format("%s %s %s", name, lastName, otherLastNames);
		return String.format("%s %s", name, lastName);
	}

	public String getProfessionsAsString() {
		String result = professions.stream().map(ProfessionCompleteDto::getDescription).collect(Collectors.joining(", "));
		return !result.isEmpty() ? result : "-";
	}

	@JsonIgnore
	public boolean hasMN() {
		return this.professions != null
				&& this.professions
					.stream()
					.anyMatch(ProfessionCompleteDto::hasMN);
	}

	@JsonIgnore
	public boolean hasMP() {
		return this.professions != null
				&& this.professions
					.stream()
					.anyMatch(ProfessionCompleteDto::hasMP);
	}

}
