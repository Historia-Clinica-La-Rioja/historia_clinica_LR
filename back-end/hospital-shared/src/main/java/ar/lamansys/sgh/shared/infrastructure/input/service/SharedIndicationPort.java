package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.time.LocalDateTime;
import java.util.List;

public interface SharedIndicationPort {

	List<DietDto> getInternmentEpisodeDiets(Integer internmentEpisodeId);

	DietDto getInternmentEpisodeDiet(Integer dietId);

	Integer addDiet(DietDto dietDto, Short sourceTypeId);

	Integer addOtherIndication(OtherIndicationDto otherIndicationDto, Short sourceTypeId);

	Integer addPharmaco(PharmacoDto pharmacoDto, Short sourceTypeId);

	Integer addParenteralPlan(ParenteralPlanDto parenteralPlanDto, Short sourceTypeId);

	List<OtherIndicationDto> getInternmentEpisodeOtherIndications(Integer internmentEpisodeId);
	OtherIndicationDto getInternmentEpisodeOtherIndication(Integer otherIndicationId);

	List<PharmacoSummaryDto> getInternmentEpisodePharmacos(Integer internmentEpisodeId);
	PharmacoDto getInternmentEpisodePharmaco(Integer pharmacoId);
	List<PharmacoSummaryDto> getMostFrequentPharmacos(Integer professionalId, Integer institutionId, Integer limit);

	List<ParenteralPlanDto> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId);
	ParenteralPlanDto getInternmentEpisodeParenteralPlan(Integer parenteralPlanId);
	List<ParenteralPlanDto> getMostFrequentParenteralPlans(Integer professionalId, Integer institutionId, Integer limit);

	List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId);

	boolean updateNursingRecordStatus(Integer nursingRecordId, String status, LocalDateTime administrationTime, Integer userId, String reason);

	void saveDocument(Long id, Integer indicationId);
}
