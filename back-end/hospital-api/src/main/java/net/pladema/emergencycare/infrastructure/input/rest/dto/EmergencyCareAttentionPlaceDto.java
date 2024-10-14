package net.pladema.emergencycare.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtySectorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareAttentionPlaceDto {

	private Integer id;
	private String description;
	private Short sectorOrganizationId;
	private Integer sectorId;
	private List<EmergencyCareDoctorsOfficeDto> doctorsOffices;
	private List<ShockroomDto> shockRooms;
	private List<EmergencyCareBedDto> beds;
	private List<ClinicalSpecialtySectorDto> clinicalSpecialtySectors;
}
