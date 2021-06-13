package ar.lamansys.sgx.shared.dates.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class DateTimeDto {

    private final DateDto date;

    private final TimeDto time;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DateTimeDto(@JsonProperty(value = "date") DateDto date,
                       @JsonProperty(value = "time") TimeDto time) {
        this.date = date;
        this.time = time;
    }
}
