package ar.lamansys.sgh.clinichistory.application.indication.creatediet;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;

import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.DietStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateDiet {

	private final DietStorage storage;

	private final NursingRecordStorage nursingRecordStorage;

	public Integer run(DietBo dietBo) {
		log.debug("Input parameter -> dietBo {}", dietBo);
		Integer result = storage.createDiet(dietBo);
		dietBo.setId(result);
		nursingRecordStorage.createNursingRecordsFromIndication(getIndicationSummary(dietBo));
		log.debug("Output -> {}", result);
		return result;
	}

	private IndicationSummaryBo getIndicationSummary(DietBo dietBo){
		return new IndicationSummaryBo(
				dietBo.getId(),
				null,
				dietBo.getDescription(),
				null
		);
	}

}
