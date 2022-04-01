package net.pladema.clinichistory.hospitalization.service.indication.pharmaco.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentIndicationBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternmentPharmacoBo extends InternmentIndicationBo {

	private SnomedBo snomed;

	private DosageBo dosage;

	private OtherPharmacoBo solvent;

	private Integer healthConditionId;

	private Integer foodRelationId;

	private Boolean patientProvided;

	private Integer viaId;

	private DocumentObservationsBo notes;

}
