package net.pladema.violencereport.infrastructure.input.rest.dto.aggressordetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.ESecurityForceType;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SecurityForceRelatedDto {

	private Boolean belongsToSecurityForces;

	private Boolean inDuty;

	private ESecurityForceType securityForceTypes;

}
