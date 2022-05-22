package net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentIndicationBo;

import java.util.List;

@Getter
@Setter
public class InternmentParenteralPlanBo extends InternmentIndicationBo {

	private SnomedBo snomed;

	private FrequencyBo frequency;

	private DosageBo dosage;

	private Short via;

	private List<OtherPharmacoBo> pharmacos;

}
