package net.pladema.snowstorm.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SnomedTemplateSearchVo {

	private Integer groupId;

	private String description;

	private Integer snomedId;

	private String sctid;

	private String pt;

}


