package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportVictimBo {

	private Boolean canReadAndWrite;

	private Boolean hasIncome;

	private Boolean worksAtFormalSector;

	private Boolean hasSocialPlan;

	private Boolean hasDisability;

	private Short disabilityCertificateStatusId;

	private Boolean isInstitutionalized;

	private String institutionalizedDetails;

	private Boolean lackOfLegalCapacity;

	private ViolenceReportActorBo keeperData;

}
