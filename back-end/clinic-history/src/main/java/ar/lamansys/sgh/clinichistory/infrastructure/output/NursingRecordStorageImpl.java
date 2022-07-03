package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.NursingRecordRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication.NursingRecord;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class NursingRecordStorageImpl implements NursingRecordStorage {

	private static final Short PENDING_STATUS_ID = EIndicationStatus.PENDING.getId();

	private final NursingRecordRepository nursingRecordRepository;

	@Override
	public List<Integer> createNursingRecordsFromIndication(IndicationSummaryBo indication) {
		log.debug("Input parameter -> nursingRecordBo {}", indication.toString());
		List<Integer> result = fromIndication(indication)
				.stream()
				.map( nr -> { return nursingRecordRepository.save(nr).getId();})
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return null;
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
