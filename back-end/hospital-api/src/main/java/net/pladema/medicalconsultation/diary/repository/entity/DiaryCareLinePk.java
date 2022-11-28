package net.pladema.medicalconsultation.diary.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DiaryCareLinePk implements Serializable {

	@Column(name = "diary_id", nullable = false)
	private Integer diaryId;

	@Column(name = "care_line_id", nullable = false)
	private Integer careLineId;
}