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
@Table(name = "z_score")
public class ZScore {

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

	@Column(name = "sd3_negative", nullable = false)
	private Double sd3Negative;

	@Column(name = "sd2_negative", nullable = false)
	private Double sd2Negative;

	@Column(name = "sd1_negative", nullable = false)
	private Double sd1Negative;

	@Column(name = "sd0", nullable = false)
	private Double sd0;

	@Column(name = "sd1", nullable = false)
	private Double sd1;

	@Column(name = "sd2", nullable = false)
	private Double sd2;

	@Column(name = "sd3", nullable = false)
	private Double sd3;

	@Column(name = "anthropometric_graphic_id", nullable = false)
	private Short anthropometricGraphicId;

	@Column(name = "gender_id", nullable = false)
	private Short genderId;

	@Column(name = "time_period_id", nullable = false)
	private Short timePeriodId;

}
