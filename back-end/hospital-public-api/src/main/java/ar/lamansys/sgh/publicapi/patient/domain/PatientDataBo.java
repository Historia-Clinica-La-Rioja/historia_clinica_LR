package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PatientDataBo {
	private Integer id;
	private String lastName;
	private String otherLastName;
	private String firstName;
	private String middleNames;
	private LocalDate birthDate;
	private String phonePrefix;
	private String phoneNumber;
	private GenderSelfDeterminationBo genderSelfDetermination;
	private String nameSelfDetermination;

}
