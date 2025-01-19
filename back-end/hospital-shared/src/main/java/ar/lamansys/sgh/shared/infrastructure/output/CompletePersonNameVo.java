package ar.lamansys.sgh.shared.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompletePersonNameVo {

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private String selfDeterminateName;

}
