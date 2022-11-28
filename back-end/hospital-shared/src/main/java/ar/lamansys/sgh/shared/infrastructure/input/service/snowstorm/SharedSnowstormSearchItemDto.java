package ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SharedSnowstormSearchItemDto {

	private String id;
	private String conceptId;
	private Boolean active;
	private String pt;

}

