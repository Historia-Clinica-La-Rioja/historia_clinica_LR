package ar.lamansys.sgh.shared.infrastructure.input.service.datastructures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageRequestDto {

	private Integer pageNumber;

	private Integer pageSize;

}

