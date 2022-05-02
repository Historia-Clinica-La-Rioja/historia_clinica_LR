package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PharmacoSummaryDto extends IndicationDto implements Serializable {

	private SharedSnomedDto snomed;

	private NewDosageDto dosage;

	private String via;

	public PharmacoSummaryDto(Integer id, Integer patientId,
							  Short typeId, Short statusId,
							  Integer professionalId, String createdByName,
							  DateDto indicationDate, DateTimeDto createOn,
							  SharedSnomedDto snomedDto, NewDosageDto dosageDto, String via) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), professionalId, createdByName, indicationDate, createOn);
		this.snomed = snomedDto;
		this.dosage = dosageDto;
		this.via = via;
	}
}
