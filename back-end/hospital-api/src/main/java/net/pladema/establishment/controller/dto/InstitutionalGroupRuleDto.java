package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionalGroupRuleDto {

	private Integer id;
	private Integer ruleId;
	private Integer clinicalSpecialtyId;
	private Integer snomedId;
	private Integer institutionalGroupId;
	private String ruleName;
	private String ruleLevel;
	private boolean regulated;
	private String comment;

}
