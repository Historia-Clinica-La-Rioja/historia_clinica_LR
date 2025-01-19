package net.pladema.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDataBo {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    private String identificationType;

    private String identificationNumber;

    private Integer userId;

    @Nullable
    private String username;

	private Integer personId;

	public PersonDataBo(Integer userId, @Nullable String username) {
		this.userId = userId;
		this.username = username;
	}

	public PersonDataBo(String firstName, String lastName, String identificationType,
						String identificationNumber, Integer userId, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
		this.userId = userId;
		this.username = username;
	}

	public PersonDataBo(Integer userId, Integer personId) {
		this.userId = userId;
		this.personId = personId;
	}
}