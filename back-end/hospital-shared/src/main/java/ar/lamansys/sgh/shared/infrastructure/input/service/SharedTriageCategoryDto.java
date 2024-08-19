package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.domain.TriageCategoryBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SharedTriageCategoryDto {

	private String name;

	private String colorCode;

	public SharedTriageCategoryDto(TriageCategoryBo triageCategory) {
		this.name = triageCategory.getName();
		this.colorCode = triageCategory.getColorCode();
	}

}
