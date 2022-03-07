package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.EIndicationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.EIndicationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DietDto extends IndicationDto {

	@NotNull(message = "{value.mandatory}")
	private String description;

	public DietDto(Integer id, Integer patientId, Short typeId, Short statusId, Integer createdBy, LocalDateTime indicationDate, String description) {
		super(id, patientId, EIndicationType.map(typeId), EIndicationStatus.map(statusId), createdBy, indicationDate);
		this.description = description;
	}

}
