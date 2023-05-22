package net.pladema.cipres.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class AnthropometricDataBo {

	private String bloodType;

	private String height;

	private String weight;

	private String bmi;

	private String headCircumference;

}
