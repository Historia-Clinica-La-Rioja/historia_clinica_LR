package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "violence_report_keeper")
@Entity
public class ViolenceReportKeeper implements Serializable {

	private static final long serialVersionUID = -8971262556717818961L;

	@Id
	@Column(name = "report_id")
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

	@Column(name = "relationship_with_victim_id", nullable = false)
	private Short relationshipWithVictimId;

	@Column(name = "other_relationship_with_victim", length = 30)
	private String otherRelationshipWithVictim;

}
