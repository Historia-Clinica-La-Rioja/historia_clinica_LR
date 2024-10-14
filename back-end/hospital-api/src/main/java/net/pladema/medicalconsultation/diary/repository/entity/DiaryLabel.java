package net.pladema.medicalconsultation.diary.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.service.domain.DiaryLabelBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "diary_label")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryLabel {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "diary_id", nullable = false)
	private Integer diaryId;

	@Column(name = "color_id", nullable = false)
	private Short colorId;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	public DiaryLabel(DiaryLabelBo diaryLabelBo) {
		this.diaryId = diaryLabelBo.getDiaryId();
		this.colorId = diaryLabelBo.getColorId();
		this.description = diaryLabelBo.getDescription();
	}
}
