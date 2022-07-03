package ar.lamansys.sgh.clinichistory.application.indication.createotherindication;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.application.ports.OtherIndicationStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherIndicationBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOtherIndication {

	private final OtherIndicationStorage storage;

	private final NursingRecordStorage nursingRecordStorage;

	public Integer run(OtherIndicationBo otherIndicationBo) {
		log.debug("Input parameter -> otherIndicationBo {}", otherIndicationBo);
		Integer result = storage.createOtherIndication(otherIndicationBo);
		otherIndicationBo.setId(result);
		nursingRecordStorage.createNursingRecordsFromIndication(getIndicationSummary(otherIndicationBo));
		log.debug("Output -> {}", result);
		return result;
	}

	private IndicationSummaryBo getIndicationSummary(OtherIndicationBo otherIndicationBo){
		return new IndicationSummaryBo(
				otherIndicationBo.getId(),
				otherIndicationBo.getDosage(),
				otherIndicationBo.getDescription(),
				otherIndicationBo.getIndicationDate()
		);
	}

}
