package ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeotherindications;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeOtherIndications {

	private final OtherIndicationStorage storage;

	public List<OtherIndicationBo> run(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<OtherIndicationBo> result = storage.getInternmentEpisodeOtherIndications(internmentEpisodeId);
		log.debug("Output -> {}", result);
		return result;
	}
}
