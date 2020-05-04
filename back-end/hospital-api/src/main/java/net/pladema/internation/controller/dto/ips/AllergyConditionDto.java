package net.pladema.internation.controller.dto.ips;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AllergyConditionDto extends HealthConditionDto {

    private String categoryId;

    private String severity;

    @NotNull(message = "{value.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}
