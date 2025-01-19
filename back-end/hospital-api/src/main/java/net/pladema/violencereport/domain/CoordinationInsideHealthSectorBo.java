package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoordinationInsideHealthSectorBo {

	private CoordinationActionBo healthSystemOrganization;

	private CoordinationActionBo healthInstitutionOrganization;

	private Short wereInternmentIndicatedId;

}
