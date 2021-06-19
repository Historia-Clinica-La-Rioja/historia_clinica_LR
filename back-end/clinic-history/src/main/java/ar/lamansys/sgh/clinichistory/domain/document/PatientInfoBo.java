package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientInfoBo{

	private Integer id;

	private Short genderId;

	private Short age;

}