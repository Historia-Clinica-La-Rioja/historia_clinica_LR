package net.pladema.clinichistory.hospitalization.service.indication.otherindication.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentIndicationBo;

@Getter
@Setter
public class InternmentOtherIndicationBo extends InternmentIndicationBo {

	private Short otherIndicationTypeId;

	private String description;

	private DosageBo dosageBo;

	private String otherType;

}
