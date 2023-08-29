package net.pladema.medicalconsultation.diary.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DiaryProtectedAppointmentsSearch {

	@NotNull
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

	private EAppointmentModality modality;

	public DiaryProtectedAppointmentsSearch(Integer careLineId, Integer clinicalSpecialtyId, Integer departmentId, @Nullable Integer institutionId,
											LocalDate initialSearchDate, LocalDate endSearchDate, Boolean includeNameSelfDetermination) {
		this.careLineId = careLineId;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.departmentId = departmentId;
		this.institutionId = institutionId;
		this.initialSearchDate = initialSearchDate;
		this.endSearchDate = endSearchDate;
		this.includeNameSelfDetermination = includeNameSelfDetermination;
	}

}
