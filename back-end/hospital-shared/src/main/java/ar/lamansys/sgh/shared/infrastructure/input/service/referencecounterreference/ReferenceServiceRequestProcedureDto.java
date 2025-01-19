package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReferenceServiceRequestProcedureDto {

	private Integer serviceRequestId;

	private SharedSnomedDto procedure;

	private String category;

}
