package net.pladema.medicalconsultation.diary.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class DiaryADto {

    @NotNull
    private Integer healthcareProfessionalId;

    @NotNull
    private Integer doctorsOfficeId;

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String endDate;

    @NotNull
    private Short appointmentDuration;

    @Nullable
    private boolean automaticRenewal = false;

    @Nullable
    private boolean professionalAssignShift = false;

    @Nullable
    private boolean includeHoliday = false;

    @NotNull
    @Valid
    private List<DiaryOpeningHoursDto> diaryOpeningHours;

	@NotNull
	private Integer clinicalSpecialtyId;

	@NotNull
	private List<Integer> diaryAssociatedProfessionalsId;

	@Nullable
	private String alias;

	@Nullable
	private List<Integer> careLines;

	@NotNull
	private Integer protectedAppointmentsPercentage;
}
