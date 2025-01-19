package ar.lamansys.sgx.shared.dates.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class TimeDto {

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

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TimeDto(@JsonProperty(value = "hours") Integer hours,
                   @JsonProperty(value = "minutes") Integer minutes,
                   @JsonProperty(value = "seconds") Integer seconds
    ) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

	@Override
	public String toString() {
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TimeDto timeDto = (TimeDto) o;
		return Objects.equals(hours, timeDto.hours) && Objects.equals(minutes, timeDto.minutes) && Objects.equals(seconds, timeDto.seconds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hours, minutes, seconds);
	}
}
