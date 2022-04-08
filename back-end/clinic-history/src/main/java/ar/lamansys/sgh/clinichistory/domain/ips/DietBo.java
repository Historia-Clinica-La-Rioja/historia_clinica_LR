package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DietBo extends IndicationBo {

	private String description;

	public DietBo(Integer id, Integer patientId, Short typeId, Short statusId, Integer createdBy, Integer professionalId, LocalDate indicationDate, LocalDateTime createdOn, String description) {
		super(id, patientId, typeId, statusId, createdBy, professionalId, indicationDate, createdOn);
		this.description = description;
	}
}
