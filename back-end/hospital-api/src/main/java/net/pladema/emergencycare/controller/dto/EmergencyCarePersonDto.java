package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.controller.dto.PersonPhotoDto;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCarePersonDto {

	private Integer id;
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private String identificationNumber;
	private String nameSelfDetermination;
	private String identificationType;
	private String gender;
	private PersonAgeDto age;
	@Nullable
	private PersonPhotoDto photo;
}
