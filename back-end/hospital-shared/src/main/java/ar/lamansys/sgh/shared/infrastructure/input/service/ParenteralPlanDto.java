package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParenteralPlanDto extends IndicationDto {

	@NotNull(message = "{value.mandatory}")
	private SharedSnomedDto snomed;

	@NotNull(message = "{value.mandatory}")
	private NewDosageDto dosage;

	@NotNull(message = "{value.mandatory}")
	private FrequencyDto frequency;

	@Nullable
	private Short via;

	@NotNull(message = "{value.mandatory}")
	private List<OtherPharmacoDto> pharmacos;

	public ParenteralPlanDto(Integer id, Integer patientId, Short typeId, Short statusId, Integer professionalId, String createdBy, DateDto indicationDate, DateTimeDto createdOn, SharedSnomedDto snomed, NewDosageDto dosage, FrequencyDto frequency, Short via, List<OtherPharmacoDto> pharmacos) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), professionalId, createdBy, indicationDate, createdOn);
		this.snomed = snomed;
		this.dosage = dosage;
		this.frequency = frequency;
		this.pharmacos = pharmacos;
		this.via = via;
	}

}
