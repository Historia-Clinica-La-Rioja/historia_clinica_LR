package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FrequencyDto {

	@Nullable
	private Integer id;

	@Nullable
	private TimeDto duration;

	@NotNull(message = "{value.mandatory}")
	private Integer flowMlHour;

	@NotNull(message = "{value.mandatory}")
	private Float flowDropsHour;

	@Nullable
	private Float dailyVolume;

}
