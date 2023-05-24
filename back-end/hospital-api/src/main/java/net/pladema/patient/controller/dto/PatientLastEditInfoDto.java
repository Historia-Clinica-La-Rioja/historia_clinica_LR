package net.pladema.patient.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
public class PatientLastEditInfoDto {

	private LocalDateTime updatedOn;

	private String updatedBy;

	private boolean wasEdited;

	public PatientLastEditInfoDto(LocalDateTime updatedOn, String updatedBy, boolean wasEdited) {
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
		this.wasEdited = wasEdited;
	}
}
