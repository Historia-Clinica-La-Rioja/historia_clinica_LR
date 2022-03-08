package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedIndicationPort {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);

	Integer addDiet(DietDto dietDto);

	void saveDocument(Long id, Integer indicationId);
}
