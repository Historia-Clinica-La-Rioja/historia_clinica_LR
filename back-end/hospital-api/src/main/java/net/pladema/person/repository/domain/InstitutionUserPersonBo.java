package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionUserPersonBo {

	private Integer userId;

	private Integer personId;

	private Integer institutionId;

	private String completeName;

	private String completeLastName;

	private String identificationNumber;

	public InstitutionUserPersonBo(Integer institutionId, Integer userId, Integer personId,
								   String firstName, String middleNames, String lastName,
								   String otherLastNames, String identificationNumber) {
		this.userId = userId;
		this.personId = personId;
		this.institutionId = institutionId;
		this.completeName = Stream.of(firstName, middleNames).filter(Objects::nonNull).collect(Collectors.joining(" "));
		this.completeLastName = Stream.of(lastName, otherLastNames).filter(Objects::nonNull).collect(Collectors.joining(" "));
		this.identificationNumber = identificationNumber;
	}

	public InstitutionUserPersonBo(Integer userId, Integer personId,
								   String firstName, String middleNames, String lastName,
								   String otherLastNames, String identificationNumber) {
		this.userId = userId;
		this.personId = personId;
		this.completeName = Stream.of(firstName, middleNames).filter(Objects::nonNull).collect(Collectors.joining(" "));
		this.completeLastName = Stream.of(lastName, otherLastNames).filter(Objects::nonNull).collect(Collectors.joining(" "));
		this.identificationNumber = identificationNumber;
	}

}