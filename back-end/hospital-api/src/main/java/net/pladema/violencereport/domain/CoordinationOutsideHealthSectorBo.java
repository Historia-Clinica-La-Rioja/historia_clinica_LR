package net.pladema.violencereport.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoordinationOutsideHealthSectorBo {

	private List<Short> municipalGovernmentDeviceIds;

	private List<Short> provincialGovernmentDeviceIds;

	private List<Short> nationalGovernmentDeviceIds;

	private Boolean withOtherSocialOrganizations;

}
