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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@Entity
@Table(name = "minsal_lr_questionnaire_response", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireResponseII {

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

	@JsonIgnore
	@Column(name = "created_by")
	private Integer createdBy;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Transient
	private String createdByFullName;

	@Transient
	private String createdByLicenseNumber;

	@JsonIgnore
	@Column(name = "updated_by")
	private Integer updatedBy;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "updated_on", nullable = false)
	private LocalDateTime updatedOn;

	@Transient
	private String updatedByFullName;

	@Transient
	private String updatedByLicenseNumber;

	@JsonIgnore
	@Column(name = "deleted", nullable = false)
	private Boolean deleted;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;

	@Column(name = "deleted_by")
	private Integer deletedBy;

	@Setter
	@Transient
	private String questionnaireType;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
	private QuestionnaireII questionnaireData;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private User createdByUser;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "updated_by", insertable = false, updatable = false)
	private User updatedByUser;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private HealthcareProfessional createdByHealthcareProfessional;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "updated_by", insertable = false, updatable = false)
	private HealthcareProfessional updatedByHealthcareProfessional;

	public String getQuestionnaireType() {
		if (questionnaireData != null) {
			return questionnaireData.getName();
		} else {
			return null;
		}
	}

	public String getCreatedByLicenseNumber() {
		if (createdByHealthcareProfessional != null) {
			return createdByHealthcareProfessional.getLicenseNumber();
		} else  {
			return null;
		}
	}

	public String getUpdatedByLicenseNumber() {
		if (updatedByHealthcareProfessional != null) {
			return updatedByHealthcareProfessional.getLicenseNumber();
		} else  {
			return null;
		}
	}

}
