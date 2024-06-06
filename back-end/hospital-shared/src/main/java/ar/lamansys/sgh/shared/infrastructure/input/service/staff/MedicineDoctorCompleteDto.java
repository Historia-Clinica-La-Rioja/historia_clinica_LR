package ar.lamansys.sgh.shared.infrastructure.input.service.staff;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MedicineDoctorCompleteDto {

	private String firstName;
	private String middleNames;
	private String lastName;
	private String nameSelfDetermination;
	private String otherLastNames;
	private String identificationType;
	private String identificationNumber;
	private List<ProfessionCompleteDto> professions;

}
