package net.pladema.clinichistory.documents.service.domain;

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