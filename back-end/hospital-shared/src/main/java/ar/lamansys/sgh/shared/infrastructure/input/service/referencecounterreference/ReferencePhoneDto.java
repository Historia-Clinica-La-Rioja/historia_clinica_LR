package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReferencePhoneDto {

	private String phoneNumber;
	private String phonePrefix;

}
