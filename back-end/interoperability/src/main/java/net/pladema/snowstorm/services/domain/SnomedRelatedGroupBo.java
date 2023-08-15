package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class SnomedRelatedGroupBo {

	private Integer id;
	private Integer snomedId;
	private String snomedSctid;
	private String snomedPt;

}
