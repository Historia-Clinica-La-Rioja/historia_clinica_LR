package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class SharedAnthropometricDataDto {

	private String bloodType;

	private String height;

	private String weight;

	private String bmi;

	private String headCircumference;
}
