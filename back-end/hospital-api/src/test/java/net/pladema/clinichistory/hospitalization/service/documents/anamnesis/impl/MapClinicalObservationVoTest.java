package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MapClinicalObservationVoTest {

	@BeforeEach
	void setUp() {
	}


	@Test
	void testMapRiskFactors() {
		List<ClinicalObservationVo> resultQuery = new ArrayList<>();
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		resultQuery.addAll(mockRiskFactor(ObservationStatus.FINAL, dateTime.plusMinutes(1)));
		resultQuery.addAll(mockRiskFactor(ObservationStatus.FINAL, dateTime.plusMinutes(2)));
		resultQuery.addAll(mockRiskFactor(ObservationStatus.ERROR, dateTime.plusMinutes(3)));
		resultQuery.addAll(mockRiskFactor(ObservationStatus.ERROR, dateTime.plusMinutes(4)));
		resultQuery.addAll(mockRiskFactor(ObservationStatus.FINAL, dateTime.plusMinutes(5)));

		MapClinicalObservationVo mapClinicalObservationVo = new MapClinicalObservationVo(resultQuery);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();
		mapClinicalObservationVo.getClinicalObservationByCode().forEach((k, v) -> {

			assertThat(v)
					.isNotNull()
					.isNotEmpty()
					.hasSize(1);

			assertRiskFactorsVo(v);
		});

	}

	private void assertRiskFactorsVo(List<ClinicalObservationVo> clinicalObservationVos) {
		for (ClinicalObservationVo clinicalObservationVo : clinicalObservationVos) {
			assertThat(clinicalObservationVo)
					.isNotNull();

			assertThat(clinicalObservationVo.getId())
					.isNotNull();
		}
	}


	private List<ClinicalObservationVo> mockRiskFactor(String status, LocalDateTime localDateTime){
		List<ClinicalObservationVo> riskFactors = new ArrayList<>();
		for (int i=0;i<1;i++){
			ClinicalObservationVo diastolic = new ClinicalObservationVo(i+1, ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), status,"1", localDateTime.plusSeconds(i));
			riskFactors.add(diastolic);

			ClinicalObservationVo systolic = new ClinicalObservationVo(i+2, ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), status, "11123", localDateTime.plusSeconds(i));
			riskFactors.add(systolic);

			ClinicalObservationVo temperature = new ClinicalObservationVo(i+3, ERiskFactor.TEMPERATURE.getSctidCode(), status,"14", localDateTime.plusSeconds(i));
			riskFactors.add(temperature);

			ClinicalObservationVo respiratoryRate = new ClinicalObservationVo(i+4, ERiskFactor.RESPIRATORY_RATE.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			riskFactors.add(respiratoryRate);

			ClinicalObservationVo heartRate = new ClinicalObservationVo(i+5, ERiskFactor.HEART_RATE.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			riskFactors.add(heartRate);

			ClinicalObservationVo mean = new ClinicalObservationVo(i+6, ERiskFactor.MEAN_PRESSURE.getSctidCode(), status,"1", localDateTime.plusSeconds(i));
			riskFactors.add(mean);

			ClinicalObservationVo bloodOxygen = new ClinicalObservationVo(i+7, ERiskFactor.BLOOD_OXYGEN_SATURATION.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			riskFactors.add(bloodOxygen);
		}
		return riskFactors;
	}

}
