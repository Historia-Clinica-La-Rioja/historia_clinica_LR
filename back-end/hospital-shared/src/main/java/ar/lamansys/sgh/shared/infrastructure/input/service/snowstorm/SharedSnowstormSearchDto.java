package ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SharedSnowstormSearchDto {

	private List<SharedSnowstormSearchItemDto> items;
	private Integer limit;
	private Integer total;
	private String searchAfter;

}

