package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DiaryProtectedAppointmentsSearch {

	@Nullable
	private Integer careLineId;

	@NotNull
	private Integer clinicalSpecialtyId;

	@NotNull
	private Integer departmentId;

	@Nullable
	private Integer institutionId;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate initialSearchDate;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate endSearchDate;

	private Boolean includeNameSelfDetermination = false;

}
