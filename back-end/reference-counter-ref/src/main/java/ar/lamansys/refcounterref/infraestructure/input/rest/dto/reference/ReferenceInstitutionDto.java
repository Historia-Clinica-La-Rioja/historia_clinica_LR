package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ReferenceInstitutionDto {

	private Integer id;

	private String description;

	private Integer departmentId;

	private String departmentName;

	private String provinceName;

}
