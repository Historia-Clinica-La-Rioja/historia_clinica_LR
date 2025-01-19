package net.pladema.procedure.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedPracticeVo {

	private Integer id;
	private String sctid;
	private String pt;

}
