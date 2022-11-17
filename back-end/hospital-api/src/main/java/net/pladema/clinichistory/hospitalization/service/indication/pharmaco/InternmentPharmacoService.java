package net.pladema.clinichistory.hospitalization.service.indication.pharmaco;

import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.domain.InternmentPharmacoBo;

import java.util.List;

public interface InternmentPharmacoService {

	Integer add(InternmentPharmacoBo pharmacoBo);

	List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId);
	PharmacoDto getInternmentEpisodePharmaco(Integer pharmacoId);

}
