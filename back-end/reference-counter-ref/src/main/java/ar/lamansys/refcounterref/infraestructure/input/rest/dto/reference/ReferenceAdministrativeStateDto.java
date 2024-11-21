package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceAdministrativeState;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceAdministrativeStateDto {

	private Integer id;
	private Integer referenceId;
	private EReferenceAdministrativeState state;
	private String reason;
	private String professionalName;
	private DateTimeDto createdOn;

}
