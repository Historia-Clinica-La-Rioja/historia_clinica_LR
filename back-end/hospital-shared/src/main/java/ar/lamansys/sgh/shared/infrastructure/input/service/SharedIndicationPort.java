package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedIndicationPort {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);
}
