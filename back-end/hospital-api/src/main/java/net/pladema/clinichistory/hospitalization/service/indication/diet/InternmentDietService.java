package net.pladema.clinichistory.hospitalization.service.indication.diet;

import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;

import java.util.List;

public interface InternmentDietService {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);
}
