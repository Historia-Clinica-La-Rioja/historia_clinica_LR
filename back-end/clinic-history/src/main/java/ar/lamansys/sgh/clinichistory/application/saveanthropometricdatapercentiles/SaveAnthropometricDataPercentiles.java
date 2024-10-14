package ar.lamansys.sgh.clinichistory.application.saveanthropometricdatapercentiles;

import ar.lamansys.sgh.clinichistory.application.ports.DocumentPercentilesStorage;
import ar.lamansys.sgh.clinichistory.application.ports.PercentilesStorage;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.PercentilesBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.enums.EAnthropometricGraphic;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class SaveAnthropometricDataPercentiles {
	private static final Integer MAX_PATIENT_AGE = 18;

	private final PercentilesStorage percentilesStorage;
	private final SharedPatientPort sharedPatientPort;
	private final DocumentPercentilesStorage documentPercentilesStorage;

	public void run(Integer patientId, Long documentId, AnthropometricDataBo anthropometricData){
		log.debug("Input parameters -> patientId {}, documentId {}, anthropometricData {}", patientId, documentId, anthropometricData);
		if (anthropometricData != null) {
			BasicPatientDto patientInfo = sharedPatientPort.getBasicDataFromPatient(patientId);
			if (!isPatientValid(patientInfo))
				return;
			saveAnthropometricDataPercentiles(documentId, patientInfo, anthropometricData);
		}
	}

	private boolean isPatientValid(BasicPatientDto patientInfo){
		if (patientInfo.getGender() == null || patientInfo.getGender().getId().equals(EGender.X.getId()))
			return false;
		if (patientInfo.getBirthDate() == null || ChronoUnit.YEARS.between(LocalDate.now(), patientInfo.getBirthDate()) > MAX_PATIENT_AGE)
			return false;
		return true;
	}

	private void saveAnthropometricDataPercentiles(Long documentId, BasicPatientDto patientInfo, AnthropometricDataBo anthropometricDataBo){
		EGender gender = EGender.map(patientInfo.getGender().getId());
		long ageInMonths = ChronoUnit.MONTHS.between(patientInfo.getBirthDate(), LocalDate.now());
		long ageInYears = ChronoUnit.YEARS.between(patientInfo.getBirthDate(), LocalDate.now());
		if (isValueValid(anthropometricDataBo.getHeight()))
			savePercentileValueByAge(documentId, anthropometricDataBo.getHeight().getValue(), ageInMonths, EAnthropometricGraphic.LENGTH_HEIGHT_FOR_AGE, gender);
		if (isValueValid(anthropometricDataBo.getWeight()))
			savePercentileValueByAge(documentId, anthropometricDataBo.getWeight().getValue(), ageInMonths, EAnthropometricGraphic.WEIGHT_FOR_AGE, gender);
		if (isValueValid(anthropometricDataBo.getHeadCircumference()))
			savePercentileValueByAge(documentId, anthropometricDataBo.getHeadCircumference().getValue(), ageInMonths, EAnthropometricGraphic.HEAD_CIRCUMFERENCE, gender);
		ClinicalObservationBo bmi = anthropometricDataBo.getBMI();
		if (isValueValid(bmi) && !bmi.getValue().equals("-")) {
			savePercentileValueByAge(documentId, bmi.getValue().replace(',', '.'), ageInMonths, EAnthropometricGraphic.BMI_FOR_AGE, gender);
			savePercentileValueByHeight(documentId, anthropometricDataBo.getWeight().getValue().replace(',', '.'), anthropometricDataBo.getHeight().getValue(), ageInYears, gender);
		}
	}

	private boolean isValueValid(ClinicalObservationBo clinicalObservationBo){
		return clinicalObservationBo != null && clinicalObservationBo.getValue() != null;
	}

	private void savePercentileValueByAge(Long documentId, String value, long ageInMonths, EAnthropometricGraphic graphic, EGender gender){
		List<PercentilesBo> percentiles = percentilesStorage.getPercentilesList(graphic, gender);
		percentiles.stream().filter(p -> p.getXValue().equals((double)ageInMonths)).findFirst().ifPresent(percentile -> {
			Double percentileValue = getPercentileValue(percentile, Double.parseDouble(value));
			documentPercentilesStorage.save(documentId, percentile.getId(), percentileValue);
		});
	}
	private void savePercentileValueByHeight(Long documentId, String weight, String height, long ageInYears, EGender gender) {
		List<PercentilesBo> percentiles = new ArrayList<>();
		if (ageInYears < EAnthropometricGraphic.WEIGHT_FOR_HEIGHT.getMinAge()){
			percentiles = percentilesStorage.getPercentilesList(EAnthropometricGraphic.WEIGHT_FOR_LENGTH, gender);
		}
		if (ageInYears <= EAnthropometricGraphic.WEIGHT_FOR_HEIGHT.getMaxAge()){
			percentiles = percentilesStorage.getPercentilesList(EAnthropometricGraphic.WEIGHT_FOR_HEIGHT, gender);
		}
		if (!percentiles.isEmpty()){
			percentiles.stream().filter(p -> Math.abs(Double.parseDouble(weight) - p.getXValue()) <= 0.2d).findFirst().ifPresent(percentile -> {
				Double percentileValue = getPercentileValue(percentile, Double.parseDouble(height));
				documentPercentilesStorage.save(documentId, percentile.getId(), percentileValue);
			});
		}
	}

	private Double getPercentileValue(PercentilesBo percentile, Double observationValue){
		if (observationValue >= percentile.getP3() && observationValue < percentile.getP10())
			return percentile.getP3();
		else if (observationValue < percentile.getP25())
			return percentile.getP10();
		else if (observationValue < percentile.getP50())
			return percentile.getP25();
		else if (observationValue < percentile.getP75())
			return percentile.getP50();
		else if (observationValue < percentile.getP90())
			return percentile.getP75();
		else if (observationValue < percentile.getP97())
			return percentile.getP90();
		else if (observationValue >= percentile.getP97())
			return percentile.getP97();
		return 0d;
	}

}
