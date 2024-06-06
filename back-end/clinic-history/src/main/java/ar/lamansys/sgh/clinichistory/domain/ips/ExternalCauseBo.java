package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EEventLocation;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ExternalCauseVo;
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

	public ExternalCauseBo(ExternalCauseVo vo){
		setId(vo.getId());
		setSnomed(vo.getSnomed()!= null ? new SnomedBo(vo.getSnomed()) : null);
		setExternalCauseType(vo.getExternalCauseTypeId() != null ? EExternalCauseType.map(vo.getExternalCauseTypeId()) : null);
		setEventLocation(vo.getEventLocation() != null ? EEventLocation.map(vo.getEventLocation()) : null);
	}

	public boolean hasNotNullValues(){
		return externalCauseType != null ||
				eventLocation != null ||
				snomed != null;
	}

}
