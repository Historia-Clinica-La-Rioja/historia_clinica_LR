package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceForwardingType;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReferenceForwardingDto {

	private Integer id;

	private String createdBy;

	private Integer userId;

	private String observation;

	private EReferenceForwardingType type;

	private DateTimeDto date;

}
