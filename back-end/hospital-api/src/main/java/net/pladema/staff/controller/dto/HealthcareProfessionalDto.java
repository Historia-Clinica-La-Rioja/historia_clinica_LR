package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;

@Setter
@Getter
public class HealthcareProfessionalDto {

	private Integer id;
	private String licenseNumber;
	// Se utiliza para filtro de historico de evoluciones
	private Integer personId;
	private PersonBasicDataResponseDto person;
	private String nameSelfDetermination;
	
}
