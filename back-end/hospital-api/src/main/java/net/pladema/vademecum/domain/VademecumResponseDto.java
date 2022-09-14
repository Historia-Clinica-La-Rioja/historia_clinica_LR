package net.pladema.vademecum.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class VademecumResponseDto {

	private List<SnomedDto> items;
	private Long total;
}
