package net.pladema.clinichistory.indication.service.pharmaco;

import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import net.pladema.clinichistory.indication.service.pharmaco.domain.GenericPharmacoBo;

import java.util.List;

public interface PharmacoService {

	Integer add(GenericPharmacoBo pharmacoBo, Short sourceTypeId);

	List<PharmacoSummaryDto> getEpisodePharmacos(Integer internmentEpisodeId, Short sourceTypeId);
	PharmacoDto getPharmaco(Integer pharmacoId);

}
