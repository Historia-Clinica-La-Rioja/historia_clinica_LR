package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InstitutionalGroupInstitutionDto {

	private Integer id;
	private Integer institutionId;
	private Integer institutionalGroupId;
	private String institutionName;
	private String departmentName;

}
