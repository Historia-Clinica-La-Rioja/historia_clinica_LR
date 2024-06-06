package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PersonInfoExtendedBo {

	private String middleNames;
	private String otherLastNames;
	private String email;
	private String nameSelfDetermination;
	private Short genderSelfDeterminationId;
}
