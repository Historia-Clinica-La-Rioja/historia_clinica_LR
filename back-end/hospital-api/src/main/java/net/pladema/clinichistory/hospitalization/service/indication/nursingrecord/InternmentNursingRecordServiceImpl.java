package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord;

import ar.lamansys.sgh.shared.infrastructure.input.service.NursingRecordDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedIndicationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class
InternmentNursingRecordServiceImpl implements InternmentNursingRecordService{

	private final SharedIndicationPort sharedIndicationPort;
	
	private final LocalDateMapper localDateMapper;

	@Override
	public List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<NursingRecordDto> result = sharedIndicationPort.getInternmentEpisodeNursingRecords(internmentEpisodeId);
		log.debug("Output -> {}", result.toString());
		return result;
	}
}
