package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InstitutionalGroupDto {

	private Integer id;
	private Short typeId;
	private String name;
	private String institutions;

}
