package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BedNurseDto {
	private Integer userId;
	private Integer personId;
	private String fullName;
	private String identificationNumber;
}
