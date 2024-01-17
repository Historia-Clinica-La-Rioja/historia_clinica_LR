package net.pladema.medicalconsultation.diary.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class DiaryAppointmentsSearchBo {

	@Nullable
	private Integer careLineId;

	@Nullable
	private List<Integer> clinicalSpecialtyIds;

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

	@Nullable
	private Integer practiceId;

	private Boolean regulationProtected = false;

	public DiaryAppointmentsSearchBo(Integer careLineId, List<Integer> clinicalSpecialtyIds, Integer departmentId,
									 Integer institutionId, LocalDate initialSearchDate, LocalDate endSearchDate,
									 Boolean includeNameSelfDetermination, EAppointmentModality modality, Integer practiceId) {
		this.careLineId = careLineId;
		this.clinicalSpecialtyIds = clinicalSpecialtyIds;
		this.departmentId = departmentId;
		this.institutionId = institutionId;
		this.initialSearchDate = initialSearchDate;
		this.endSearchDate = endSearchDate;
		this.includeNameSelfDetermination = includeNameSelfDetermination;
		this.modality = modality;
		this.practiceId = practiceId;
	}
	
}
