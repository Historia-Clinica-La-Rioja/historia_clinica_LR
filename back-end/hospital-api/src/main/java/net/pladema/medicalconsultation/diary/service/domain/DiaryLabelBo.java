package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryLabel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryLabelBo {

	private Integer id;

	private Integer diaryId;

	private Short colorId;

	private String description;

	public DiaryLabelBo(DiaryLabel diaryLabel) {
		this.id = diaryLabel.getId();
		this.diaryId = diaryLabel.getDiaryId();
		this.colorId = diaryLabel.getColorId();
		this.description = diaryLabel.getDescription();
	}
}
