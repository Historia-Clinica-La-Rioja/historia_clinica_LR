package net.pladema.parameter.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.parameter.infrastructure.output.repository.entity.ParameterTextOption;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ParameterTextOptionBo {

	private Integer id;
	private Integer parameterId;
	private String description;

	public ParameterTextOptionBo(ParameterTextOption entity){
		this.id = entity.getId();
		this.parameterId = entity.getParameterId();
		this.description = entity.getDescription();
	}

}
