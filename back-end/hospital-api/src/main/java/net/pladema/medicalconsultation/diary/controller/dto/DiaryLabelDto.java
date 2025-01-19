package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

@Getter
@Setter
@NoArgsConstructor
public class DiaryLabelDto {

	private Integer id;

	private Integer diaryId;

	private Short colorId;

	private String description;

	public DiaryLabelDto(DiaryLabelBo diaryLabelBo) {
		this.id = diaryLabelBo.getId();
		this.diaryId = diaryLabelBo.getDiaryId();
		this.colorId = diaryLabelBo.getColorId();
		this.description = diaryLabelBo.getDescription();
	}
}
