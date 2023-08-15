package net.pladema.snowstorm.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SnomedRelatedGroupDto {

	private Integer id;
	private Integer snomedId;
	private String snomedSctid;
	private String snomedPt;

}
