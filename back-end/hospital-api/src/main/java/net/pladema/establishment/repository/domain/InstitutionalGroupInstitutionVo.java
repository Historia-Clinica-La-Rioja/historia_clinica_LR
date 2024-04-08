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
public class InstitutionalGroupInstitutionVo {

	private Integer id;
	private Integer institutionId;
	private Integer institutionalGroupId;
	private String institutionName;
	private String departmentName;

}
