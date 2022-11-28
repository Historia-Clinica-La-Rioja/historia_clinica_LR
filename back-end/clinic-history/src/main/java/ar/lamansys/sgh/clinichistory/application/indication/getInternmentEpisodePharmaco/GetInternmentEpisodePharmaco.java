package ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodePharmaco;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
@Slf4j
@RequiredArgsConstructor

public class GetInternmentEpisodePharmaco {
	private final PharmacoStorage storage;

	public PharmacoBo run(Integer pharmacoId) {
		log.debug("Input parameter -> pharmacoId {}", pharmacoId);
		PharmacoBo result = storage.findById(pharmacoId)
				.orElseThrow(() -> new EntityNotFoundException("pharmaco.invalid.id"));
		log.debug("Output -> {}", result);
		return result;
	}

}
