package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OtherIndicationBo extends IndicationBo {

	private Short otherIndicationTypeId;

	private DosageBo dosage;

	private String description;

	private String otherType;

	public OtherIndicationBo(Integer id, Integer patientId, Short typeId, Short statusId, Integer createdBy, Integer professionalId, LocalDate indicationDate, LocalDateTime createdOn, Short otherIndicationTypeId, DosageBo dosage, String description, String otherType) {
		super(id, patientId, typeId, statusId, createdBy, professionalId, indicationDate, createdOn);
		this.otherIndicationTypeId = otherIndicationTypeId;
		this.dosage = dosage;
		this.description = description;
		this.otherType = otherType;
	}
}
