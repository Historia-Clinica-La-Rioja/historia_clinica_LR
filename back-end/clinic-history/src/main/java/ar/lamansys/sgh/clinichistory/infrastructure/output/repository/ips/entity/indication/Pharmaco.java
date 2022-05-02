package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pharmaco")
@Getter
@Setter
@NoArgsConstructor
public class Pharmaco extends Indication {

	private static final long serialVersionUID = 1127149048573033190L;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "dosage_id", nullable = false)
	private Integer dosageId;

	@Column(name = "health_condition_id", nullable = false)
	private Integer healthConditionId;

	@Column(name = "food_relation_id", nullable = false)
	private Short foodRelationId;

	@Column(name = "patient_provided", nullable = false)
	private Boolean patientProvided;

	@Column(name = "via", nullable = false)
	private Short viaId;

}
