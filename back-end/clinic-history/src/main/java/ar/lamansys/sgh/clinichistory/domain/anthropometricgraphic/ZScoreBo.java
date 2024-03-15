package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.ETimePeriod;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ZScoreBo {

	private Double xValue;
	private Double l;
	private Double m;
	private Double s;
	private Double sd;
	private Double sd3negative;
	private Double sd2negative;
	private Double sd1negative;
	private Double sd0;
	private Double sd1;
	private Double sd2;
	private Double sd3;
	private EAnthropometricGraphic anthropometricGraphic;
	private EGender gender;
	private ETimePeriod timePeriod;

}
