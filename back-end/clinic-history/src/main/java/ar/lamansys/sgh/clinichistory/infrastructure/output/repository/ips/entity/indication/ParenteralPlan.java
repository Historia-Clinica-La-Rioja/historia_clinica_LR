package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "parenteral_plan")
@Getter
@Setter
@NoArgsConstructor
public class ParenteralPlan extends Indication {

	@Column(name = "snomed_id", nullable = false )
	private Integer snomedId;

	@Column(name = "dosage_id", nullable = false)
	private Integer dosageId;

	@Column(name = "frequency_id", nullable = false)
	private Integer frequencyId;

	@Column(name = "via")
	private Short viaId;

}
