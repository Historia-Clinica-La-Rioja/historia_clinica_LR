package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "other_pharmaco")
@Getter
@Setter
@NoArgsConstructor
public class OtherPharmaco {

	private static final long serialVersionUID = -929158940653118877L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "indication_id", nullable = false)
	private Integer indicationId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "dosage_id", nullable = false)
	private Integer dosageId;

}
