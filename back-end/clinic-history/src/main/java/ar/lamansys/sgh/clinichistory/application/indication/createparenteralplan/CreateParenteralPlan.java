package ar.lamansys.sgh.clinichistory.application.indication.createparenteralplan;

import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CreateParenteralPlan {

	private final ParenteralPlanStorage storage;

	private final NursingRecordStorage nursingRecordStorage;

	public Integer run(ParenteralPlanBo parenteralPlanBo) {
		log.debug("Input parameter -> parenteralPlanBo {}", parenteralPlanBo.toString());
		Integer result = storage.createParenteralPlan(parenteralPlanBo);
		parenteralPlanBo.setId(result);
		nursingRecordStorage.createNursingRecordsFromIndication(getIndicationSummary(parenteralPlanBo));
		log.debug("Output -> {}", result);
		return result;
	}

	private IndicationSummaryBo getIndicationSummary(ParenteralPlanBo bo){
		return new IndicationSummaryBo(
				bo.getId(),
				bo.getDosage(),
				null,
				bo.getIndicationDate()
		);
	}

}
