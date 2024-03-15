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
public class PercentilesBo {

	private Double xValue;
	private Double p3;
	private Double p10;
	private Double p25;
	private Double p50;
	private Double p75;
	private Double p90;
	private Double p97;
	private EAnthropometricGraphic anthropometricGraphic;
	private EGender gender;
	private ETimePeriod timePeriod;

}
