package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.DietStorage;
import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HistoricNursingRecordRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.NursingRecordRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.HistoricNursingRecordStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.NursingRecord;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.HospitalUserPersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class NursingRecordStorageImpl implements NursingRecordStorage {

	private static final Short PENDING_STATUS_ID = ENursingRecordStatus.PENDING.getId();
	private static final String OUTPUT = "Output -> {}";

	private final NursingRecordRepository nursingRecordRepository;
	private final IndicationRepository indicationRepository;
	private final DietStorage dietStorage;
	private final ParenteralPlanStorage parenteralPlanStorage;
	private final OtherIndicationStorage otherIndicationStorage;
	private final PharmacoStorage pharmacoStorage;
	private final HistoricNursingRecordRepository historicNursingRecordRepository;
	private final SharedHospitalUserPort sharedHospitalUserPort;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public List<Integer> createNursingRecordsFromIndication(IndicationSummaryBo indication) {
		log.debug("Input parameter -> nursingRecordBo {}", indication.toString());
		List<Integer> result = fromIndication(indication)
				.stream()
				.map( nr -> { return nursingRecordRepository.save(nr).getId();})
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<NursingRecordBo> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<NursingRecordBo> result = nursingRecordRepository.getByInternmentEpisodeId(internmentEpisodeId).
				stream().map(this::mapToBo).collect(Collectors.toList());
		result.forEach(this::setLastUpdateInfo);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean updateStatus(Integer id, Short statusId, LocalDateTime administrationTime, Integer userId, String reason) {
		log.debug("Input parameter -> id {}, statusId {}, administrationTime {}, userId {}, reason {}", id, statusId, administrationTime, userId, reason);
		nursingRecordRepository.updateStatus(id, statusId, userId, administrationTime);
		historicNursingRecordRepository.save(new HistoricNursingRecordStatus(id, statusId, reason));
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Optional<Integer> getIndicationIdById(Integer id) {
		log.debug("Input parameter -> id {} ", id);
		Optional<Integer> indicationId = nursingRecordRepository.getIndicationIdById(id);
		log.debug(OUTPUT, indicationId.get());
		return indicationId;
	}

	@Override
	public List<NursingRecordBo> getIndicationNursingRecords(Integer indicationId) {
		log.debug("Input parameter -> indicationId {}", indicationId);
		List<NursingRecordBo> result = nursingRecordRepository.getByIndicationId(indicationId).
				stream().map(this::mapToBo).collect(Collectors.toList());
		result.forEach(this::setLastUpdateInfo);
		log.debug(OUTPUT, result);
		return result;
	}

	private void setLastUpdateInfo(NursingRecordBo nursingRecordBo){
		historicNursingRecordRepository.getAllByNursingRecordId(nursingRecordBo.getId()).stream().findFirst().ifPresent(entity -> nursingRecordBo.setUpdateReason(entity.getReason()));
		HospitalUserPersonInfoDto p = sharedHospitalUserPort.getUserCompleteInfo(nursingRecordBo.getUpdatedBy());
		if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && p.getNameSelfDetermination() != null && !p.getNameSelfDetermination().isEmpty())
			nursingRecordBo.setUpdatedByName(p.getNameSelfDetermination() + " " + p.getLastName());
		else
			nursingRecordBo.setUpdatedByName(p.getFirstName() + " " + p.getLastName());
	}

	private NursingRecordBo mapToBo(NursingRecord entity){
		NursingRecordBo result = new NursingRecordBo();
		result.setId(entity.getId());
		result.setIndication(getIndication(entity.getIndicationId()));
		result.setStatusId(entity.getStatusId());
		result.setScheduledAdministrationTime(entity.getScheduledAdministrationTime());
		result.setAdministrationTime(entity.getAdministrationTime());
		result.setEvent(entity.getEvent());
		result.setObservation(entity.getObservation());
		result.setUpdatedBy(entity.getUpdatedBy());
		return result;
	}

	private IndicationBo getIndication(Integer indicationId){
		Optional<Short> indicationType = indicationRepository.getTypeById(indicationId);
		switch(EIndicationType.map(indicationType.get())){
			case DIET:{
				DietBo dietBo = dietStorage.findById(indicationId).get();
				return dietBo;
			}
			case OTHER_INDICATION:{
				OtherIndicationBo oiBo = otherIndicationStorage.findById(indicationId).get();
				return oiBo;
			}
			case PARENTERAL_PLAN:{
				ParenteralPlanBo ppBo = parenteralPlanStorage.findById(indicationId).get();
				return ppBo;
			}
			default:{
				PharmacoBo pharmacoBo = pharmacoStorage.findById(indicationId).get();
				return pharmacoBo;
			}
		}
	}

	private List<NursingRecord> fromIndication(IndicationSummaryBo indication) {
		List<NursingRecord> result = new ArrayList<>();
		if (indication.getDosage() == null || indication.getDosage().getPeriodUnit() == null) {
			result.add(new NursingRecord(null, indication.getId(), null, null, PENDING_STATUS_ID, indication.getObservation(), null));
		} else {
			result.addAll(getRecordsByPeriodUnit(indication));
		}
		return result;
	}

	private List<NursingRecord> getRecordsByPeriodUnit(IndicationSummaryBo indication) {
		List<NursingRecord> result = new ArrayList<>();
		switch(EUnitsOfTimeBo.map(indication.getDosage().getPeriodUnit())) {
			/* Indicación por intervalos */
			case HOUR: {
				result.addAll(getRecordsWithFrequency(indication));
				break;
			}
			/* Indicación única vez */
			case DAY: {
				result.add(new NursingRecord(null, indication.getId(), indication.getDosage().getStartDate(), null, PENDING_STATUS_ID, indication.getObservation(), null));
				break;
			}
			/* Indicación ante evento */
			case EVENT: {
				result.add(new NursingRecord(null, indication.getId(), null, indication.getDosage().getEvent(), PENDING_STATUS_ID, indication.getObservation(), null));
				break;
			}
			default: { break; }
		}
		return result;
	}

	private List<NursingRecord> getRecordsWithFrequency (IndicationSummaryBo indication){
		List<NursingRecord> records = new ArrayList<>();
		ZonedDateTime administrationTime = indication.getDosage().getStartDate().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		ZonedDateTime endTime = administrationTime.plusDays(1).toLocalDate().atStartOfDay().atZone(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		while (administrationTime.isBefore(endTime)) {
			NursingRecord record = new NursingRecord();
			record.setIndicationId(indication.getId());
			record.setObservation(indication.getObservation());
			record.setScheduledAdministrationTime(administrationTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
			record.setStatusId(PENDING_STATUS_ID);
			records.add(record);
			administrationTime = administrationTime.plusHours(indication.getDosage().getFrequency().longValue());
		}
		return records;
	}

}
