package net.pladema.clinichistory.indication.service.parenteralplan.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.indication.service.diet.domain.GenericIndicationBo;

import java.util.List;

@Getter
@Setter
public class GenericParenteralPlanBo extends GenericIndicationBo {

	private SnomedBo snomed;

	private FrequencyBo frequency;

	private DosageBo dosage;

	private Short via;

	private List<OtherPharmacoBo> pharmacos;

}
