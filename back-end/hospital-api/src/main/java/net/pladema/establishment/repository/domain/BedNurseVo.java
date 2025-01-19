package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BedNurseVo {
	private Integer userId;
	private Integer personId;
	private String fullName;
	private String identificationNumber;

	public BedNurseVo(Integer userId, Integer personId, String identificationNumber) {
		this.userId = userId;
		this.personId = personId;
		this.identificationNumber = identificationNumber;
	}
}
