package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCareEpisodeListTriageDto {

	private Integer id;
	private String name;
	//colorCode
	private String color;
	private List<OutpatientReasonDto> reasons;
	private ProfessionalPersonDto creator;
	private EmergencyCareClinicalSpecialtySectorDto clinicalSpecialtySector;
	private DateTimeDto createdOn;
}
