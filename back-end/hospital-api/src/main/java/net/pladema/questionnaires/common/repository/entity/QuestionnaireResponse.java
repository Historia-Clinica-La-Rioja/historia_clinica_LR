package net.pladema.questionnaires.common.repository.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Entity
@Table(name = "minsal_lr_questionnaire_response", schema = "public")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"creationable", "updateable", "deleteable", "createdBy", "updatedBy"})
public class QuestionnaireResponse extends SGXAuditableEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonIgnore
	@Column(name = "questionnaire_id", nullable = false)
	private Integer questionnaireId;

	@Column(name = "status_id", nullable = false)
	private Integer statusId;

	@JsonIgnore
	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Transient
	private String createdByFullName;

	@Transient
	private String createdByLicenseNumber;

	@Transient
	private String updatedByFullName;

	@Transient
	private String updatedByLicenseNumber;

	@Setter
	@Transient
	private Integer questionnaireTypeId;

	@Setter
	@Transient
	private String questionnaireType;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
	private Questionnaire questionnaireData;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private HealthcareProfessional createdByHealthcareProfessional;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "updated_by", insertable = false, updatable = false)
	private HealthcareProfessional updatedByHealthcareProfessional;

	@JsonIgnore
	@OneToMany(mappedBy = "questionnaireResponse", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers = new ArrayList<>();

	public Integer getQuestionnaireTypeId() {
		if (questionnaireData != null) {
			return questionnaireData.getId();
		} else {
			return null;
		}
	}

	public String getQuestionnaireType() {
		if (questionnaireData != null) {
			return questionnaireData.getName();
		} else {
			return null;
		}
	}

}
