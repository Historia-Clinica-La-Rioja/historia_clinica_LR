package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PharmacoBo extends IndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private OtherPharmacoBo solvent;

	private Integer healthConditionId;

	private Integer foodRelationId;

	private Boolean patientProvided;

	private Integer viaId;

	private String note;

	public PharmacoBo(Integer id, Integer patientId, Short typeId,
					  Short statusId, Integer createdBy, Integer professionalId,
					  LocalDate indicationDate, LocalDateTime createdOn,
					  SnomedBo snomed, DosageBo dosage, OtherPharmacoBo solvent,
					  Integer healthConditionId, Integer foodRelationId, Boolean patientProvided,
					  Integer viaId, String note) {
		super(id, patientId, typeId, statusId, createdBy, professionalId, indicationDate, createdOn);
		this.snomed = snomed;
		this.dosage = dosage;
		this.solvent = solvent;
		this.healthConditionId = healthConditionId;
		this.foodRelationId = foodRelationId;
		this.patientProvided = patientProvided;
		this.viaId = viaId;
		this.note = note;
	}

}
