package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportActorBo {

	private String lastName;

	private String firstName;

	private Short age;

	private String address;

	private Short municipalityId;

	private Short relationshipWithVictimId;

	private String otherRelationshipWithVictim;

}
