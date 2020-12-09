package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "dosage")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Dosage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "sequence")
	private Integer sequence;

	@Column(name = "count")
	private Integer count;

	@Column(name = "duration")
	private Double duration;

	@Column(name = "duration_unit", length = 20)
	private String durationUnit;

	@Column(name = "frequency")
	private Integer frequency;

	@Column(name = "period_unit", length = 20)
	private String periodUnit;

	@Column(name = "dose_quantity_id")
	private Long doseQuantityId;

}
