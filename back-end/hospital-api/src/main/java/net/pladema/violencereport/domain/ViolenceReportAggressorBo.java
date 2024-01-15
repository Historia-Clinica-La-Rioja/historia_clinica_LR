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

	public ViolenceReportAggressorBo(String lastName, String firstName, Short age, String address, Short municipalityId, String municipalityName, Short relationshipWithVictimId,
									 String otherRelationshipWithVictim, Boolean hasGuns, Boolean hasBeenTreated, Boolean belongsToSecurityForces, Boolean inDuty,
									 Short securityForceTypeId, Short livesWithVictimId, Short relationshipLengthId, Short violenceViolenceFrequencyId,
									 Short hasPreviousEpisodesId) {
		aggressorData = new ViolenceReportActorBo(lastName, firstName, age, address, municipalityId, municipalityName, relationshipWithVictimId, otherRelationshipWithVictim);
		this.hasGuns = hasGuns;
		this.hasBeenTreated = hasBeenTreated;
		this.belongsToSecurityForces = belongsToSecurityForces;
		this.inDuty = inDuty;
		this.securityForceTypeId = securityForceTypeId;
		this.livesWithVictimId = livesWithVictimId;
		this.relationshipLengthId = relationshipLengthId;
		this.violenceViolenceFrequencyId = violenceViolenceFrequencyId;
		this.hasPreviousEpisodesId = hasPreviousEpisodesId;
	}

	public ViolenceReportAggressorBo(String lastName, String firstName, Short age, String address, Short municipalityId, Short relationshipWithVictimId,
									 String otherRelationshipWithVictim, Boolean hasGuns, Boolean hasBeenTreated, Boolean belongsToSecurityForces, Boolean inDuty,
									 Short securityForceTypeId, Short livesWithVictimId, Short relationshipLengthId, Short violenceViolenceFrequencyId,
									 Short hasPreviousEpisodesId) {
		aggressorData = new ViolenceReportActorBo(lastName, firstName, age, address, municipalityId, relationshipWithVictimId, otherRelationshipWithVictim);
		this.hasGuns = hasGuns;
		this.hasBeenTreated = hasBeenTreated;
		this.belongsToSecurityForces = belongsToSecurityForces;
		this.inDuty = inDuty;
		this.securityForceTypeId = securityForceTypeId;
		this.livesWithVictimId = livesWithVictimId;
		this.relationshipLengthId = relationshipLengthId;
		this.violenceViolenceFrequencyId = violenceViolenceFrequencyId;
		this.hasPreviousEpisodesId = hasPreviousEpisodesId;
	}

}
