package ar.lamansys.sgh.publicapi.activities.domain.datetimeutils;

import lombok.Getter;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Getter
public class TimeBo {
	@NotNull
	@Min(value = 0)
	@Max(value = 23)
	private final Integer hours;

	@NotNull
	@Min(value = 0)
	@Max(value = 59)
	private final Integer minutes;

	@Nullable
	@Min(value = 0)
	@Max(value = 59)
	private final Integer seconds;

	public TimeBo(Integer hours,
				  Integer minutes,
				  Integer seconds
	) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
}
