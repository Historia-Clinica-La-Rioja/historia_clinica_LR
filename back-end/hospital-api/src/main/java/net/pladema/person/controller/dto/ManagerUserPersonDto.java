package net.pladema.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.repository.domain.ManagerUserPersonBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManagerUserPersonDto {

	private Integer id;
	private Integer personId;
	private String searchText;
	private String completeName;
	private String identificationNumber;
	private String role;

	public ManagerUserPersonDto(ManagerUserPersonBo searchUserPersonBo) {
		this.id = searchUserPersonBo.getUserId();
		this.personId = searchUserPersonBo.getPersonId();
		this.completeName = searchUserPersonBo.getCompleteName();
		this.identificationNumber = searchUserPersonBo.getIdentificationNumber();
	}
}
