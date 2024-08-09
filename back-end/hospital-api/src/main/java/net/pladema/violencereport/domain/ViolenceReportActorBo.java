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

	private ViolenceReportAddressBo address;

	private Short relationshipWithVictimId;

	private String otherRelationshipWithVictim;

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String homeAddress, Short municipalityId,
								 String municipalityName, Integer cityId, String cityName, Short relationshipWithVictimId,
								 String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = initializeAddress(null, municipalityId, municipalityName, cityId, cityName, homeAddress);
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String homeAddress, Short provinceId, Short municipalityId,
								 String municipalityName, Integer cityId, String cityName, Short relationshipWithVictimId,
								 String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = initializeAddress(provinceId, municipalityId, municipalityName, cityId, cityName, homeAddress);
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}

	public ViolenceReportActorBo(String lastName, String firstName, Short age, String homeAddress, Short municipalityId, Integer cityId,
								 Short relationshipWithVictimId, String otherRelationshipWithVictim) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.age = age;
		this.address = initializeAddress(null, municipalityId, null, cityId, null, homeAddress);
		this.relationshipWithVictimId = relationshipWithVictimId;
		this.otherRelationshipWithVictim = otherRelationshipWithVictim;
	}

	private ViolenceReportAddressBo initializeAddress(Short provinceId, Short municipalityId, String municipalityName, Integer cityId,
													  String cityName, String homeAddress) {
		return ViolenceReportAddressBo.builder()
				.provinceId(provinceId)
				.municipalityId(municipalityId)
				.municipalityName(municipalityName)
				.cityId(cityId)
				.cityName(cityName)
				.homeAddress(homeAddress)
				.build();
	}

}
