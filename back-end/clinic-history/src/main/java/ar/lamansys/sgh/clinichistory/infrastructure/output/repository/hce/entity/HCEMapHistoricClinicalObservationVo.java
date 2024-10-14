package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EObservationLab;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ERiskFactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HCEMapHistoricClinicalObservationVo {

	private static final Logger LOG = LoggerFactory.getLogger(HCEMapClinicalObservationVo.class);

	public static final String OUTPUT = "Output -> {}";

	private Map<Long, List<HCEClinicalObservationVo>> clinicalObservationByDocument;

	public HCEMapHistoricClinicalObservationVo(List<HCEClinicalObservationVo> clinicalObservationVos){
		super();
		clinicalObservationByDocument = new TreeMap<>(clinicalObservationVos.stream().collect(Collectors.groupingBy(HCEClinicalObservationVo::getDocumentId)));
		clinicalObservationByDocument.forEach(this::processData);
	}

	private void processData(Long key, List<HCEClinicalObservationVo> inputValues) {
		LOG.debug("Input parameters -> key {}, RiskFactors {}", key, inputValues);
		List<HCEClinicalObservationVo> result = new ArrayList<>();
		int historyLength = 2;
		int index = 0;
		int deleteQuantity = 0;
		while (historyLength > 0 && index < inputValues.size()) {
			while (index < inputValues.size()) {
				if (!inputValues.get(index).hasError() && deleteQuantity == 0) {
					result.add(inputValues.get(index));
					historyLength--;
				}
				if (!inputValues.get(index).hasError() && deleteQuantity > 0) {
					deleteQuantity--;
				}
				if (inputValues.get(index).hasError())
					deleteQuantity++;
				index++;
			}
		}
		inputValues.clear();
		inputValues.addAll(result);
		LOG.debug(OUTPUT, result);
	}

	public List<HCEAnthropometricDataBo> getHistoricAnthropometricData(){
		List<HCEAnthropometricDataBo> result = new ArrayList<>();
		clinicalObservationByDocument.forEach((documentId, observationList) -> {
			HCEAnthropometricDataBo anthropometricDataBo = new HCEAnthropometricDataBo();
			observationList.forEach(clinicalObservation -> {
				if (clinicalObservation.getSctidCode().equals(ERiskFactor.HEIGHT.getSctidCode()))
					anthropometricDataBo.setHeight(new HCEClinicalObservationBo(clinicalObservation));
				if (clinicalObservation.getSctidCode().equals(ERiskFactor.WEIGHT.getSctidCode()))
					anthropometricDataBo.setWeight(new HCEClinicalObservationBo(clinicalObservation));
				if (clinicalObservation.getSctidCode().equals(EObservationLab.BLOOD_TYPE.getSctidCode()))
					anthropometricDataBo.setBloodType(new HCEClinicalObservationBo(clinicalObservation));
				if (clinicalObservation.getSctidCode().equals(ERiskFactor.HEAD_CIRCUMFERENCE.getSctidCode()))
					anthropometricDataBo.setHeadCircumference(new HCEClinicalObservationBo(clinicalObservation));
			});
			if (anthropometricDataBo.hasValues())
				result.add(anthropometricDataBo);
		});
		return result;
	}

}
