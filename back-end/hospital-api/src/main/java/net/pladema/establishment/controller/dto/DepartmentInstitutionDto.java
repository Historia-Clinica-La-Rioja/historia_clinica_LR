package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Institution;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DepartmentInstitutionDto {

	private Integer id;
	private Short departmentId;
	private Short provinceId;
	private String name;

	public DepartmentInstitutionDto(Institution institution){
		this.id = institution.getId();
		this.name = institution.getName();
	}

}
