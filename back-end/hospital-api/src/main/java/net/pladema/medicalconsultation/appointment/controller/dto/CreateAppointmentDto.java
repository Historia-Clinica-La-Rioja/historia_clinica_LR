package net.pladema.medicalconsultation.appointment.controller.dto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.TranscribedDiagnosticReportInfoDto;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

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

	private String patientEmail;

	@NotNull(message = "{value.mandatory}")
	private EAppointmentModality modality;

	@Nullable
	private TranscribedDiagnosticReportInfoDto transcribedOrderData;

	@Nullable
	private DiagnosticReportInfoDto orderData;
}
