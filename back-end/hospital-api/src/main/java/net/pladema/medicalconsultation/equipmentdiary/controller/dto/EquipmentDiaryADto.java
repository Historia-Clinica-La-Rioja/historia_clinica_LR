package net.pladema.medicalconsultation.equipmentdiary.controller.dto;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;
@Getter
@Setter
@ToString
public class EquipmentDiaryADto {

	@NotNull
	private Integer equipmentId;

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
	private boolean includeHoliday = false;

	@NotNull
	@Valid
	private List<EquipmentDiaryOpeningHoursDto> equipmentDiaryOpeningHours;



}
