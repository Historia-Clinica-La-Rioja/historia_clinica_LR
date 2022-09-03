package net.pladema.medicalconsultation.diary.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CompleteDiaryDto extends DiaryDto {

    private Integer sectorId;

	private String specialtyName;
	private List<CareLineDto> careLinesInfo;

}
