package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

	@Column(name = "chronic", nullable = false)
	private Boolean chronic = false;

	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "suspended_start_date")
	private LocalDate suspendedStartDate;

	@Column(name = "suspended_end_date")
	private LocalDate suspendedEndDate;

	@Column(name = "event", length = 100)
	private String event;


	public Dosage(Double duration, String durationUnit,
				  Integer frequency, String periodUnit, Boolean chronic,
				  LocalDateTime startDate, LocalDateTime endDate,
				  LocalDate suspendedStartDate, LocalDate suspendedEndDate, String event) {
		this.duration = duration;
		this.durationUnit = durationUnit;
		this.frequency = frequency;
		this.periodUnit = periodUnit;
		this.chronic = chronic;
		this.startDate = startDate;
		this.endDate = endDate;
		this.suspendedStartDate = suspendedStartDate;
		this.suspendedEndDate = suspendedEndDate;
		this.event = event;
	}
}
