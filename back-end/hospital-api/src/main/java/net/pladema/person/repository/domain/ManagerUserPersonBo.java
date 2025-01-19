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
public class ManagerUserPersonBo {

	private Integer userId;
	private Integer personId;
	private String completeName;
	private String identificationNumber;
	private String role;

	public ManagerUserPersonBo(Integer userId, Integer personId, String firstName, String middleNames, String lastName, String otherLastNames, String identificationNumber){
		this.userId = userId;
		this.personId = personId;
		this.completeName = Stream.of(firstName, middleNames, lastName, otherLastNames).filter(Objects::nonNull).collect(Collectors.joining(" "));
		this.identificationNumber = identificationNumber;
	}

}
