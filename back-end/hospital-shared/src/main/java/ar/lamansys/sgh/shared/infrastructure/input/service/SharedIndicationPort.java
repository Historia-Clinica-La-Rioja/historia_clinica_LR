package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedIndicationPort {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);

	Integer addDiet(DietDto dietDto);

	Integer addOtherIndication(OtherIndicationDto otherIndicationDto);

	List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId);

	void saveDocument(Long id, Integer indicationId);
}
