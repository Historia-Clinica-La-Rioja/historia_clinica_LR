package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OtherRiskFactorVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EBodyTemperature;
import ar.lamansys.sgh.shared.infrastructure.input.service.EMuscleHypertonia;
import ar.lamansys.sgh.shared.infrastructure.input.service.EPerfusion;
import ar.lamansys.sgh.shared.infrastructure.input.service.ERespiratoryRetraction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtherRiskFactorBo {

	private Short bodyTemperatureId;

	private String bodyTemperatureDescription;

	private Boolean cryingExcessive;

	private Short muscleHypertoniaId;

	private String muscleHypertoniaDescription;

	private Short respiratoryRetractionId;

	private String respiratoryRetractionDescription;

	private Boolean stridor;

	private Short perfusionId;

	private String perfusionDescription;

	public OtherRiskFactorBo(Short bodyTemperatureId, Boolean cryingExcessive, Short muscleHypertoniaId, Short respiratoryRetractionId, Boolean stridor, Short perfusionId) {
		this.bodyTemperatureId = bodyTemperatureId;
		this.cryingExcessive = cryingExcessive;
		this.muscleHypertoniaId = muscleHypertoniaId;
		this.respiratoryRetractionId = respiratoryRetractionId;
		this.stridor = stridor;
		this.perfusionId = perfusionId;
		initializeDescriptions();
	}

	public OtherRiskFactorBo(OtherRiskFactorVo otherRiskFactors) {
		this.bodyTemperatureId = otherRiskFactors.getBodyTemperatureId();
		this.cryingExcessive = otherRiskFactors.getCryingExcessive();
		this.muscleHypertoniaId = otherRiskFactors.getMuscleHypertoniaId();
		this.respiratoryRetractionId = otherRiskFactors.getRespiratoryRetractionId();
		this.stridor = otherRiskFactors.getStridor();
		this.perfusionId = otherRiskFactors.getPerfusionId();
		initializeDescriptions();
	}

	private void initializeDescriptions() {
		if (bodyTemperatureId != null)
			this.bodyTemperatureDescription = EBodyTemperature.getById(bodyTemperatureId).getDescription();
		if (muscleHypertoniaId != null)
			this.muscleHypertoniaDescription = EMuscleHypertonia.getById(muscleHypertoniaId).getDescription();
		if (respiratoryRetractionId != null)
			this.respiratoryRetractionDescription = ERespiratoryRetraction.getById(respiratoryRetractionId).getDescription();
		if (perfusionId != null)
			this.perfusionDescription = EPerfusion.getById(perfusionId).getDescription();
	}

}
