package net.pladema.clinichistory.hospitalization.controller.dto;

import java.time.LocalDateTime;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PatientDischargeDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime medicalDischargeDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private LocalDateTime administrativeDischargeDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	private LocalDateTime physicalDischargeDate;

    private short dischargeTypeId;

}
