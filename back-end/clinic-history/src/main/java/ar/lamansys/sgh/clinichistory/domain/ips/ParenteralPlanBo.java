package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class ParenteralPlanBo extends IndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private FrequencyBo frequency;

	private Short via;

	private List<OtherPharmacoBo> pharmacos;

	public ParenteralPlanBo(Integer id, Integer patientId, Short typeId, Short statusId,
							Integer createdBy, Integer professionalId, LocalDate indicationDate, LocalDateTime createdOn,
							SnomedBo snomed, DosageBo dosage, FrequencyBo frequency, Short via, List<OtherPharmacoBo> pharmacos)
	{
		super(id, patientId, typeId, statusId, createdBy, professionalId, indicationDate, createdOn);
		this.snomed = snomed;
		this.dosage = dosage;
		this.frequency = frequency;
		this.via = via;
		this.pharmacos = pharmacos;
	}

}
