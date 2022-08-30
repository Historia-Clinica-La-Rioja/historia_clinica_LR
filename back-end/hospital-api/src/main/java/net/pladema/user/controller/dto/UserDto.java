package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

	private LocalDateTime previousLogin;

	public UserDto(String email, Integer id, Integer personId, String firstName, String lastName, String nameSelfDetermination, LocalDateTime previousLogin) {
		this.email = email;
		this.id = id;
		this.personDto = new UserPersonDto(personId, firstName, lastName, nameSelfDetermination);
		this.previousLogin = previousLogin;
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
