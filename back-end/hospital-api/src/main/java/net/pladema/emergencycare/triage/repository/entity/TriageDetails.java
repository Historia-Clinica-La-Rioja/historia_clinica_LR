package net.pladema.emergencycare.triage.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.domain.TriageBo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "triage_details")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TriageDetails implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4801545912233389198L;

	@Id
	@Column(name = "triage_id")
	private Integer triageId;

	@Column(name = "body_temperature_id")
	private Short bodyTemperatureId;

	@Column(name = "crying_excessive")
	private Boolean cryingExcessive;

	@Column(name = "muscle_hypertonia_id")
	private Short muscleHypertoniaId;

	@Column(name = "respiratory_retraction_id")
	private Short respiratoryRetractionId;

	@Column(name = "stridor")
	private Boolean stridor;

	@Column(name = "perfusion_id")
	private Short perfusionId;

	public TriageDetails(TriageBo triageBo) {
		this.triageId = triageBo.getTriageId();
		this.bodyTemperatureId = triageBo.getOtherRiskFactors().getBodyTemperatureId();
		this.cryingExcessive = triageBo.getOtherRiskFactors().getCryingExcessive();
		this.muscleHypertoniaId = triageBo.getOtherRiskFactors().getMuscleHypertoniaId();
		this.respiratoryRetractionId = triageBo.getOtherRiskFactors().getRespiratoryRetractionId();
		this.stridor = triageBo.getOtherRiskFactors().getStridor();
		this.perfusionId = triageBo.getOtherRiskFactors().getPerfusionId();
	}

}
