package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReferencePhoneDto {

	private String phonePrefix;

	private String phoneNumber;

}
