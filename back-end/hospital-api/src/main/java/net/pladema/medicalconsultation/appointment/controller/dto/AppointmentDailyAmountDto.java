package net.pladema.medicalconsultation.appointment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

@Value
@Builder
@ToString
@AllArgsConstructor
public class AppointmentDailyAmountDto {

    private Integer spontaneous;

    private Integer programmedAvailable;

    private Integer programmed;

	private Integer holiday;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;
}
