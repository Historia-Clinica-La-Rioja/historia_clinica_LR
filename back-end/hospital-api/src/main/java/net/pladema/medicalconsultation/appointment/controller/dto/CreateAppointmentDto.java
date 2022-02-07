package net.pladema.medicalconsultation.appointment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    @Nullable
    Integer patientMedicalCoverageId;

	@Nullable
	@Length(max = 10, message = "{appointment.new.phonePrefix.invalid}")
	private String phonePrefix;

    @Nullable
    @Length(max = 20, message = "{appointment.new.phoneNumber.invalid}")
    private String phoneNumber;
}
