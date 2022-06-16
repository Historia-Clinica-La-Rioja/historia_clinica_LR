package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
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
}
