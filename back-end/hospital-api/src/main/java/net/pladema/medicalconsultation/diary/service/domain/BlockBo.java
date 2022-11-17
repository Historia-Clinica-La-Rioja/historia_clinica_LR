package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BlockBo {

	private LocalDate initDateDto;
	private LocalDate endDateDto;
	private LocalTime init;
	private LocalTime end;
	private Short appointmentBlockMotiveId;
	private boolean fullBlock;

	public BlockBo(LocalDate initDateDto, LocalDate endDateDto, LocalTime init, LocalTime end, Short appointmentBlockMotiveId) {
		this.initDateDto = initDateDto;
		this.endDateDto = endDateDto;
		this.init = init;
		this.end = end;
		this.appointmentBlockMotiveId = appointmentBlockMotiveId;
	}

}
