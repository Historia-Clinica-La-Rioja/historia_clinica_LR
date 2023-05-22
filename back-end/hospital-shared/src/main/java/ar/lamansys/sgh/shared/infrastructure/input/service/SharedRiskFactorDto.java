package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class SharedRiskFactorDto {

	private String systolicBloodPressure;

	private String diastolicBloodPressure;
}
