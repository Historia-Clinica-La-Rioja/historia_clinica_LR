package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import java.io.Serializable;
import java.util.List;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SnomedResponseDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4433983845725789933L;

	private Integer total;

    private List<SnomedDto> items;

}
