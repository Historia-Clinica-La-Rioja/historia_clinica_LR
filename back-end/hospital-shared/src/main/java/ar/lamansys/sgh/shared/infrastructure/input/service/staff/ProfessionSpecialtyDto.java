package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionSpecialtyDto {

	private Integer id;

	private ClinicalSpecialtyDto specialty;

	private List<LicenseNumberDto> licenses;

	public ProfessionSpecialtyDto(Integer id, ClinicalSpecialtyDto specialty, List<LicenseNumberDto> licenses) {
		this.id = id;
		this.licenses = licenses;
		this.specialty = specialty;
	}

	@JsonIgnore
	public boolean hasMN() {
		return hasLicenses() && licenses.stream().anyMatch(LicenseNumberDto::hasMN);
	}

	@JsonIgnore
	public boolean hasMP() {
		return hasLicenses() && licenses.stream().anyMatch(LicenseNumberDto::hasMP);
	}

	@JsonIgnore
	public boolean hasLicenses() {
		return licenses != null && !licenses.isEmpty();
	}
}
