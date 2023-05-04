package net.pladema.clinichistory.indication.service.otherindication;

import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import net.pladema.clinichistory.indication.service.otherindication.domain.GenericOtherIndicationBo;

import java.util.List;

public interface OtherIndicationService {

	Integer add(GenericOtherIndicationBo otherIndicationBo, Short sourceTypeId);

	List<OtherIndicationDto> getEpisodeOtherIndications(Integer internmentEpisodeId, Short sourceTypeId);
	OtherIndicationDto getOtherIndication(Integer otherIndicationId);

}
