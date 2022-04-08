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

import java.time.LocalTime;

@Entity
@Table(name = "frequency")
@Getter
@Setter
@NoArgsConstructor
public class Frequency {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="duration")
	private LocalTime duration;

	@Column(name="flow_ml_hour", nullable = false)
	private Integer flowMlHour;

	@Column(name="flow_drops_hour", nullable = false)
	private Float flowDropsHour;

	@Column(name="daily_volume")
	private Float dailyVolume;

}
