package net.pladema.cipres.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class RiskFactorBo {

	private String systolicBloodPressure;

	private String diastolicBloodPressure;

}
