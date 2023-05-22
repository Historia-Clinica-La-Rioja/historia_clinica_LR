package net.pladema.cipres.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class BasicDataPatientBo {

	private Integer id;

	private BasicDataPersonBo person;

}
