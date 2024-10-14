package net.pladema.parameter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParameterBo {

	private Integer id;
	private Integer loincId;
	private String description;
	private Short typeId;
	private Short inputCount;
	private Integer snomedGroupId;

}
