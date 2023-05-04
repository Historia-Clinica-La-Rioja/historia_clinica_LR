package net.pladema.clinichistory.indication.service.otherindication.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.indication.service.diet.domain.GenericIndicationBo;

@Getter
@Setter
public class GenericOtherIndicationBo extends GenericIndicationBo {

	private Short otherIndicationTypeId;

	private String description;

	private DosageBo dosageBo;

	private String otherType;

}
