package ar.lamansys.refcounterref.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReferencePhoneBo {

	private String phoneNumber;
	private String phonePrefix;
}
