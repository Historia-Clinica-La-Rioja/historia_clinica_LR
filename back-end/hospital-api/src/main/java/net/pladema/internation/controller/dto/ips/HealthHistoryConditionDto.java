package net.pladema.internation.controller.dto.ips;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.dates.configuration.JacksonDateFormatConfig;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HealthHistoryConditionDto extends HealthConditionDto {

    @NotNull(message = "{value.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;

    private String note;
}
