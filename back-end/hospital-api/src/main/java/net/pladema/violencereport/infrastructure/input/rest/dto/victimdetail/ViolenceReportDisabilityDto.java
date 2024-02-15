package net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EDisabilityCertificateStatus;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportDisabilityDto {

	private Boolean hasDisability;

	private EDisabilityCertificateStatus disabilityCertificateStatus;

}
