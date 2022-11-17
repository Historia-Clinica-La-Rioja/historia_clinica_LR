package ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodediet;

import ar.lamansys.sgh.clinichistory.application.ports.DietStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeDiet {

	private final DietStorage storage;

	public DietBo run(Integer dietId) {
		log.debug("Input parameter -> dietId {}", dietId);
		DietBo result = storage.findById(dietId)
				.orElseThrow(() -> new EntityNotFoundException("diet.invalid.id"));;
		log.debug("Output -> {}", result);
		return result;
	}
}