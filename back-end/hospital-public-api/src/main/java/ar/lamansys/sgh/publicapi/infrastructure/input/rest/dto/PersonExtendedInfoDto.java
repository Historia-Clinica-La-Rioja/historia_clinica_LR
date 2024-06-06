package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PersonExtendedInfoDto {

	private String middleNames;
	private String otherLastNames;
	private String email;
	private String nameSelfDetermination;
	private Short genderSelfDeterminationId;
}
