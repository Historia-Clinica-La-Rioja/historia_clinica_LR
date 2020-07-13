package net.pladema.clinichistory.hospitalization.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProbableDischargeDateDto {

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String probableDischargeDate;
}
