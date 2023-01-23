package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PrescriptionProblemDto {
	String pt;
	Integer snomedId;
	String problemType;
}
