package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EExternalCauseType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalCauseBo {

	private Integer id;

	private EExternalCauseType externalCauseType;

	private EEventLocation eventLocation;

	private SnomedBo snomed;

}
