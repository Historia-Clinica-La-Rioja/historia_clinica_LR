package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstitutionalGroupRuleVo {

	private Integer id;
	private Integer ruleId;
	private Integer institutionalGroupId;
	private String clinicalSpecialtyName;
	private String conceptPt;
	private Short ruleLevel;
	private boolean regulated;
	private String comment;

}
