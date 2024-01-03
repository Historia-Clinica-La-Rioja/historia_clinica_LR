package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportAggressorBo {

	private ViolenceReportActorBo aggressorData;

	private Boolean hasGuns;

	private Boolean hasBeenTreated;

	private Boolean belongsToSecurityForces;

	private Boolean inDuty;

	private Short securityForceTypeId;

	private Short livesWithVictimId;

	private Short relationshipLengthId;

	private Short violenceViolenceFrequencyId;

	private Short hasPreviousEpisodesId;

}
