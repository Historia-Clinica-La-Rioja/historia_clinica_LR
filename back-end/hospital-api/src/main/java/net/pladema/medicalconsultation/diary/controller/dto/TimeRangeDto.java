package net.pladema.medicalconsultation.diary.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class TimeRangeDto {

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.TIME_FORMAT)
    private String from;

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.TIME_FORMAT)
    private String to;
}
