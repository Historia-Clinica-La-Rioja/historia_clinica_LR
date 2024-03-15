package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anthropometricgraphic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "percentiles")
public class Percentiles {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "x_value", nullable = false)
	private Double xValue;

	@Column(name = "l", nullable = false)
	private Double l;

	@Column(name = "m", nullable = false)
	private Double m;

	@Column(name = "s", nullable = false)
	private Double s;

	@Column(name = "sd")
	private Double sd;

	@Column(name = "p3", nullable = false)
	private Double p3;

	@Column(name = "p10", nullable = false)
	private Double p10;

	@Column(name = "p25", nullable = false)
	private Double p25;

	@Column(name = "p50", nullable = false)
	private Double p50;

	@Column(name = "p75", nullable = false)
	private Double p75;

	@Column(name = "p90", nullable = false)
	private Double p90;

	@Column(name = "p97", nullable = false)
	private Double p97;

	@Column(name = "anthropometric_graphic_id", nullable = false)
	private Short anthropometricGraphicId;

	@Column(name = "gender_id", nullable = false)
	private Short genderId;

	@Column(name = "time_period_id", nullable = false)
	private Short timePeriodId;

}
