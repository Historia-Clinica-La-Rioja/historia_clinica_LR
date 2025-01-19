package snomed.relations.cache.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCommercialMedicationSnomedDto {

	private SnomedDto genericMedication;

	private String commercialPt;

}
