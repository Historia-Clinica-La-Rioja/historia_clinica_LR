package net.pladema.questionnaires.general.getall.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "minsal_lr_questionnaire_response", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestionnaireResponseII {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "questionnaire_id", nullable = false)
	private Integer questionnaireId;

	@Column(name = "status_id", nullable = false)
	private Integer statusId;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_on", nullable = false)
	private LocalDateTime updatedOn;

	@Column(name = "deleted", nullable = false)
	private Boolean deleted;

	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;

	@Column(name = "deleted_by")
	private Integer deletedBy;

	@ManyToOne
	@JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
	private QuestionnaireII questionnaireData;

}
