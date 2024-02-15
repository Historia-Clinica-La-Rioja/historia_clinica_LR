package net.pladema.violencereport.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ViolenceReportActorBo {

	private String lastName;

	private String firstName;

	private Short age;

	private String address;

	private Short provinceId;

	private Short municipalityId;

	private String municipalityName;

	private Short relationshipWithVictimId;

	private String otherRelationshipWithVictim;

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String address, Short municipalityId, String municipalityName, Short relationshipWithVictimId,
								 String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = address;
		this.municipalityId = municipalityId;
		this.municipalityName = municipalityName;
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String address, Short provinceId, Short municipalityId, String municipalityName,
								 Short relationshipWithVictimId, String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = address;
		this.provinceId = provinceId;
		this.municipalityId = municipalityId;
		this.municipalityName = municipalityName;
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String address, Short municipalityId, Short relationshipWithVictimId, String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = address;
		this.municipalityId = municipalityId;
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}
}
