package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CompleteDiaryDto extends DiaryDto {

    private Integer sectorId;

	private String specialtyName;
}
