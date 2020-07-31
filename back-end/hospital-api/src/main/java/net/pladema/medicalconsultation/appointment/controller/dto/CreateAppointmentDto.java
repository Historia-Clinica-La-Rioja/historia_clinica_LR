package net.pladema.medicalconsultation.appointment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentDto {

    @NotNull
    private Integer diaryId;

    @NotNull
    private Integer patientId;

    @NotNull(message = "{value.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String date;

    @NotNull(message = "{value.mandatory}")
    @JsonFormat(pattern = JacksonDateFormatConfig.TIME_FORMAT)
    private String hour;

    @NotNull
    private Integer openingHoursId;

    @NotNull
    private boolean isOverturn = false;



}
