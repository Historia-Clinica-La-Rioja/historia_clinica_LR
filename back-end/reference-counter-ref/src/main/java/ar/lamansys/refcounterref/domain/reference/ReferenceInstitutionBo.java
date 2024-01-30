package ar.lamansys.refcounterref.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReferenceInstitutionBo {

	private Integer id;

	private String description;

	private Short departmentId;

	private String departmentName;

	private String provinceName;

	public ReferenceInstitutionBo(Integer id, String description){
		this.id = id;
		this.description = description;
	}

	public ReferenceInstitutionBo(Integer id, String description, Short departmentId, String departmentName){
		this.id = id;
		this.description = description;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
	}

}
