package ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodepharamacos;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoSummaryBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodePharmacos {

	private final PharmacoStorage storage;

	public List<PharmacoSummaryBo> run(Integer internmentEpisodeId, Short sourceTypeId) {
		log.debug("Input parameter -> internmentEpisodeId {}, sourceTypeId {}", internmentEpisodeId, sourceTypeId);
		List<PharmacoSummaryBo> result = storage.getInternmentEpisodePharmacos(internmentEpisodeId, sourceTypeId);
		log.debug("Output -> {}", result);
		return result;
	}

}
