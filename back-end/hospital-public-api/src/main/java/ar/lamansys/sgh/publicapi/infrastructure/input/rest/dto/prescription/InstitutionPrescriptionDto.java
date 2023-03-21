package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionPrescriptionDto {
	String name;
	String sisaCode;
	String provinceCode;
	String address;
}
