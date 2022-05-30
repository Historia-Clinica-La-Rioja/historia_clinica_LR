package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto extends AbstractUserDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314611830338628432L;

	private String email;

	private Integer id;

	private UserPersonDto personDto;

	public UserDto(String email, Integer id, Integer personId, String firstName, String lastName, String nameSelfDetermination) {
		this.email = email;
		this.id = id;
		this.personDto = new UserPersonDto(personId, firstName, lastName, nameSelfDetermination);
	}

	public Integer getPersonId() {
		if (personDto == null)
			return null;
		return personDto.getId();
	}

	public String getFirstName() {
		if (personDto == null)
			return null;
		return personDto.getFirstName();
	}

	public String getLastName() {
		if (personDto == null)
			return null;
		return personDto.getLastName();
	}

	public String getNameSelfDetermination() {
		if (personDto == null)
			return null;
		return personDto.getNameSelfDetermination();
	}
}
