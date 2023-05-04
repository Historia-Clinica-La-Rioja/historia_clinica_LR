package net.pladema.clinichistory.indication.service.diet;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import net.pladema.clinichistory.indication.service.diet.domain.GenericDietBo;

public interface DietService {

	List<DietDto> getEpisodeDiets(Integer internmentEpisodeId, Short sourceTypeId);

	DietDto getDiet(Integer dietId);

	Integer addDiet(GenericDietBo dietBo);
}
