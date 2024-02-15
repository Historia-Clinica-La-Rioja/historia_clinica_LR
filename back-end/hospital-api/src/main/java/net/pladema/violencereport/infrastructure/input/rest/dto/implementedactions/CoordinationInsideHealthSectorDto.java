package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EHealthInstitutionOrganization;
import net.pladema.violencereport.domain.enums.EHealthSystemOrganization;
import net.pladema.violencereport.domain.enums.EIntermentIndicationStatus;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CoordinationInsideHealthSectorDto {

	private CoordinationActionDto<EHealthSystemOrganization> healthSystemOrganization;

	private CoordinationActionDto<EHealthInstitutionOrganization> healthInstitutionOrganization;

	private EIntermentIndicationStatus wereInternmentIndicated;

}
