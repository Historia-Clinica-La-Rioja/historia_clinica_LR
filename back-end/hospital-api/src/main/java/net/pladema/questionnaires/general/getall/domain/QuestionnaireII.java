package net.pladema.questionnaires.general.getall.domain;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "minsal_lr_questionnaire", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionnaireII {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "status_id", nullable = false)
	private Integer statusId;

	@Column(name = "version")
	private Integer version;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "updated_on", nullable = false)
	private LocalDateTime updatedOn;

	@Column(name = "deleted", nullable = false)
	private Boolean deleted;

	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;
}
