package ar.lamansys.sgh.clinichistory.application.indication.createpharmaco;

import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePharmaco {

	private final PharmacoStorage storage;

	public Integer run(PharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		Integer result = storage.createPharmaco(pharmacoBo);
		log.debug("Output -> {}", result);
		return result;
	}

}
