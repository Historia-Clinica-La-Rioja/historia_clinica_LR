package net.pladema.vademecum.infraestructure.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class VademecumResponseDto {

	private List<SharedSnomedDto> items;
	private Long total;
}
