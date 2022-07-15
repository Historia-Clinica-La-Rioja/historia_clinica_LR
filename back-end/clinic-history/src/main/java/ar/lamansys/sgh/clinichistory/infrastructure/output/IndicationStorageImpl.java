package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.IndicationStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndicationStorageImpl implements IndicationStorage {

	private final IndicationRepository indicationRepository;

	@Override
	public boolean updateStatus(Integer id, Short statusId, Integer userId) {
		log.debug("Input parameter -> id {}, statusId {}, userId {}", id, statusId, userId);
		indicationRepository.updateStatus(id, statusId, userId);
		log.debug("Output -> {}", Boolean.TRUE);
		return Boolean.TRUE;
	}

}
