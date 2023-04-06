package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

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
public class OtherRiskFactorVo {

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

	public OtherRiskFactorVo(Short bodyTemperatureId, Boolean cryingExcessive, Short muscleHypertoniaId, Short respiratoryRetractionId, Boolean stridor, Short perfusionId) {
		this.bodyTemperatureId = bodyTemperatureId;
		this.cryingExcessive = cryingExcessive;
		this.muscleHypertoniaId = muscleHypertoniaId;
		this.respiratoryRetractionId = respiratoryRetractionId;
		this.stridor = stridor;
		this.perfusionId = perfusionId;

		this.bodyTemperatureDescription = EBodyTemperature.getById(bodyTemperatureId).getDescription();
		this.muscleHypertoniaDescription = EMuscleHypertonia.getById(muscleHypertoniaId).getDescription();
		this.respiratoryRetractionDescription = ERespiratoryRetraction.getById(respiratoryRetractionId).getDescription();
		this.perfusionDescription = EPerfusion.getById(perfusionId).getDescription();
	}

}
