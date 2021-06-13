package net.pladema.medicalconsultation.doctorsoffice.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DoctorsOfficeDto {

    private Integer id;

    @NotNull
    private String description;

    @JsonFormat(pattern = JacksonDateFormatConfig.TIME_FORMAT)
    private String openingTime;

    @JsonFormat(pattern = JacksonDateFormatConfig.TIME_FORMAT)
    private String closingTime;
}
