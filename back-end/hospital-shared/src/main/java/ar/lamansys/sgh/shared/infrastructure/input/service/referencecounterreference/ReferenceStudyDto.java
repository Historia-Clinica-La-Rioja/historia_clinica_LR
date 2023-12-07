package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceStudyDto {

	@NotNull
	private SharedSnomedDto problem;

	@NotNull
	private SharedSnomedDto practice;

	@NotNull
	private String categoryId;

}
