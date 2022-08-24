package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedIndicationPort {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);

	Integer addDiet(DietDto dietDto);

	Integer addOtherIndication(OtherIndicationDto otherIndicationDto);

	Integer addPharmaco(PharmacoDto pharmacoDto);

	Integer addParenteralPlan(ParenteralPlanDto parenteralPlanDto);

	List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId);

	List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId);

	List<ParenteralPlanDto> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId);

	List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId);

	void saveDocument(Long id, Integer indicationId);
}
