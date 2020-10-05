package net.pladema.sgx.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.pladema.sgx.dates.controller.constraints.DateDtoValid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@DateDtoValid
public class DateDto {

    @NotNull
    @Min(value = 1)
    private final Integer day;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private final Integer month;

    @NotNull
    @Min(value = 0)
    private final Integer year;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DateDto(@JsonProperty(value = "day") Integer day,
                   @JsonProperty(value = "month") Integer month,
                   @JsonProperty(value = "year") Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
