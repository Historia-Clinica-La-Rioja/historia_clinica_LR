package ar.lamansys.sgx.shared.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
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
}
