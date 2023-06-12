package net.pladema.imagenetwork.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class StudyPacBo {

	private String studyInstanceUID;
	private Integer pacServerId;

}
