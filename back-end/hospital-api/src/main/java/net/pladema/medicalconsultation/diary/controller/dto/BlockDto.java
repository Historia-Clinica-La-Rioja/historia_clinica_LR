package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BlockDto {
	private DateDto initDateDto;
	private DateDto endDateDto;
	private TimeDto init;
	private TimeDto end;
	private Short appointmentBlockMotiveId;
	private boolean fullBlock;

	public BlockDto(DateDto initDateDto, DateDto endDateDto, TimeDto init, TimeDto end, Short appointmentBlockMotiveId) {
		this.initDateDto = initDateDto;
		this.endDateDto = endDateDto;
		this.init = init;
		this.end = end;
		this.appointmentBlockMotiveId = appointmentBlockMotiveId;
	}
}
