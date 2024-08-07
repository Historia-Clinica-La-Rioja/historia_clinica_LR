package net.pladema.parameter.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompletedParameterizedFormParameterDto {

	@NotNull
	private Integer id;
	@Nullable
	private Integer optionId;
	@Nullable
	private Double numericValue;
	@Nullable
	private String textValue;
	@Nullable
	private String conceptPt;
	@Nullable
	private String conceptSctid;

}
