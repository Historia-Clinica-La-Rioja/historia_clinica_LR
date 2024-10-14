package snomed.relations.cache.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommercialMedicationDto {

	SharedSnomedDto commercial;

	SharedSnomedDto generic;

}
