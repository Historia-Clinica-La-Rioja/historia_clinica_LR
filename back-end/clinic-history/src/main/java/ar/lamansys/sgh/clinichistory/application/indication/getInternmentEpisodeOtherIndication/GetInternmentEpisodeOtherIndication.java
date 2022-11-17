package ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodeOtherIndication;

import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeOtherIndication {
	private final OtherIndicationStorage storage;

	public OtherIndicationBo run(Integer otherIndicationId) {
		log.debug("Input parameter -> otherIndicationId {}", otherIndicationId);
		OtherIndicationBo result = storage.findById(otherIndicationId)
			.orElseThrow(() -> new EntityNotFoundException("otherIndication.invalid.id"));;
		log.debug("Output -> {}", result);
		return result;
	}
}
