package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EMunicipalGovernmentDevice;
import net.pladema.violencereport.domain.enums.ENationalGovernmentDevice;
import net.pladema.violencereport.domain.enums.EProvincialGovernmentDevice;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CoordinationOutsideHealthSectorDto {

	private List<EMunicipalGovernmentDevice> municipalGovernmentDevices;

	private List<EProvincialGovernmentDevice> provincialGovernmentDevices;

	private List<ENationalGovernmentDevice> nationalGovernmentDevices;

	private Boolean withOtherSocialOrganizations;

}
