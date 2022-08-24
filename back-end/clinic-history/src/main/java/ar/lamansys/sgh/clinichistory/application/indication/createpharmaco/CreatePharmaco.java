package ar.lamansys.sgh.clinichistory.application.indication.createpharmaco;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.application.ports.PharmacoStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PharmacoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreatePharmaco {

	private final PharmacoStorage storage;

	private final NursingRecordStorage nursingRecordStorage;

	public Integer run(PharmacoBo pharmacoBo) {
		log.debug("Input parameter -> pharmacoBo {}", pharmacoBo);
		assertContextValid(pharmacoBo);
		Integer result = storage.createPharmaco(pharmacoBo);
		pharmacoBo.setId(result);
		nursingRecordStorage.createNursingRecordsFromIndication(getIndicationSummary(pharmacoBo));
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertContextValid(PharmacoBo pharmacoBo) {
		if (pharmacoBo.getSnomed() == null)
			throw new ConstraintViolationException("El fármaco es un dato obligatorio", Collections.emptySet());
		if (pharmacoBo.getDosage().getQuantity().getValue() == null)
			throw new ConstraintViolationException("La dosis es un dato obligatorio", Collections.emptySet());
		if (pharmacoBo.getDosage().getQuantity().getUnit() == null)
			throw new ConstraintViolationException("La unidad es un dato obligatorio", Collections.emptySet());
		if (pharmacoBo.getViaId() == null)
			throw new ConstraintViolationException("La vía es un dato obligatorio", Collections.emptySet());
		if (pharmacoBo.getSolvent() != null && pharmacoBo.getSolvent().getDosage().getQuantity().getValue() == null)
			throw new ConstraintViolationException("La dosis del diluyente es un dato obligatorio", Collections.emptySet());
		if (pharmacoBo.getIndicationDate() == null)
			throw new ConstraintViolationException("La fecha de la indicación es un dato obligatorio", Collections.emptySet());

	}

	private IndicationSummaryBo getIndicationSummary(PharmacoBo pharmacoBo){
		return new IndicationSummaryBo(
				pharmacoBo.getId(),
				pharmacoBo.getDosage(),
				pharmacoBo.getNote(),
				pharmacoBo.getIndicationDate()
		);
	}

}
