package ar.lamansys.sgx.shared.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ar.lamansys.sgx.shared.dates.controller.constraints.DateDtoValid;

@Getter
@DateDtoValid
public class DateDto {

    private final Integer year;

    private final Integer month;

    private final Integer day;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DateDto(@JsonProperty(value = "year") Integer year,
                   @JsonProperty(value = "month") Integer month,
                   @JsonProperty(value = "day") Integer day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
