package ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodenursingrecords;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeNursingRecords {

	private final NursingRecordStorage storage;

	public List<NursingRecordBo> run(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<NursingRecordBo> result = storage.getInternmentEpisodeNursingRecords(internmentEpisodeId);
		log.debug("Output -> {}", result.toString());
		return result;
	}


}
