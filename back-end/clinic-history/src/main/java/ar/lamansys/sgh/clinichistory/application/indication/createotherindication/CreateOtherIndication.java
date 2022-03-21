package ar.lamansys.sgh.clinichistory.application.indication.createotherindication;

import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOtherIndication {

	private final OtherIndicationStorage storage;

	public Integer run(OtherIndicationBo otherIndicationBo) {
		log.debug("Input parameter -> otherIndicationBo {}", otherIndicationBo);
		Integer result = storage.createOtherIndication(otherIndicationBo);
		log.debug("Output -> {}", result);
		return result;
	}
}
