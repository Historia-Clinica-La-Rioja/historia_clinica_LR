package ar.lamansys.sgh.shared.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ReferencePhoneBo {

	private String phonePrefix;

	private String phoneNumber;

}
