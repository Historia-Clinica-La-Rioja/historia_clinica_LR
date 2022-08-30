package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "other_indication")
@Getter
@Setter
@NoArgsConstructor
public class OtherIndication extends Indication {

	private static final long serialVersionUID = 2873716268832417941L;

	@Column(name = "other_indication_type_id", nullable = false)
	private Short otherIndicationType;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "dosage_id", nullable = false)
	private Integer dosageId;

	@Column(name = "other_type")
	private String otherType;
}
