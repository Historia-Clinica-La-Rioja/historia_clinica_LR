package net.pladema.medicalconsultation.diary.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "diary_associated_professional")
@Getter
@Setter
@ToString
public class DiaryAssociatedProfessional implements Serializable {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "diary_id", nullable = false)
	private Integer diaryId;

	@Column(name = "healthcare_professional_id", nullable = false)
	private Integer healthcareProfessionalId;

}
