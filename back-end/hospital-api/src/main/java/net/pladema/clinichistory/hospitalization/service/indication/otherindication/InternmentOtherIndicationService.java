package net.pladema.clinichistory.hospitalization.service.indication.otherindication;

import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.domain.InternmentOtherIndicationBo;

import java.util.List;

public interface InternmentOtherIndicationService {

	Integer add(InternmentOtherIndicationBo otherIndicationBo);

	List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId);
	OtherIndicationDto getInternmentEpisodeOtherIndication(Integer otherIndicationId);

}
