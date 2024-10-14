package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "violence_report_aggressor")
@Entity
public class ViolenceReportAggressor implements Serializable {

	private static final long serialVersionUID = 1700305740621486286L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "report_id", nullable = false)
	private Integer reportId;

	@Column(name = "first_name", length = 20)
	private String firstName;

	@Column(name = "last_name", length = 20)
	private String lastName;

	@Column(name = "age")
	private Short age;

	@Column(name = "address", length = 30)
	private String address;

	@Column(name = "municipality_id")
	private Short municipalityId;

	@Column(name = "city_id")
	private Integer cityId;

	@Column(name = "relationship_with_victim_id", nullable = false)
	private Short relationshipWithVictimId;

	@Column(name = "other_relationship_with_victim", length = 30)
	private String otherRelationShipWithVictim;

	@Column(name = "has_guns")
	private Boolean hasGuns;

	@Column(name = "has_been_treated")
	private Boolean hasBeenTreated;

	@Column(name = "belongs_to_security_forces")
	private Boolean belongsToSecurityForces;

	@Column(name = "in_duty")
	private Boolean inDuty;

	@Column(name = "security_force_type_id")
	private Short securityForceTypeId;

	@Column(name = "live_together_status_id", nullable = false)
	private Short liveTogetherStatusId;

	@Column(name = "relationship_length_id", nullable = false)
	private Short relationshipLengthId;

	@Column(name = "violence_frequency_id", nullable = false)
	private Short violenceFrequencyId;

	@Column(name = "criminal_record_status_id", nullable = false)
	private Short criminalRecordStatusId;

}
