package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyBo {

	private Integer id;

	private LocalTime duration;

	private Integer flowMlHour;

	private Float flowDropsHour;

	private Float dailyVolume;

}
