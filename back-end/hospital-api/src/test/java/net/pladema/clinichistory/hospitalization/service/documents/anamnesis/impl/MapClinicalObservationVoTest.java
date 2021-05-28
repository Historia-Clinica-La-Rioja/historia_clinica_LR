package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.EVitalSign;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MapClinicalObservationVoTest {

	@Before
	public void setUp() {
	}


	@Test
	public void testMapVitalSigns() {
		List<ClinicalObservationVo> resultQuery = new ArrayList<>();
		String date = "2020-05-04 16:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, dateTime.plusMinutes(1)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, dateTime.plusMinutes(2)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, dateTime.plusMinutes(3)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, dateTime.plusMinutes(4)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, dateTime.plusMinutes(5)));

		MapClinicalObservationVo mapClinicalObservationVo = new MapClinicalObservationVo(resultQuery);

		assertThat(mapClinicalObservationVo.getClinicalObservationByCode().entrySet())
				.isNotNull()
				.isNotEmpty();
		mapClinicalObservationVo.getClinicalObservationByCode().forEach((k, v) -> {

			assertThat(v)
					.isNotNull()
					.isNotEmpty()
					.hasSize(1);

			assertVitalSignsVo(v);
		});

	}

	private void assertVitalSignsVo(List<ClinicalObservationVo> clinicalObservationVos) {
		for (ClinicalObservationVo clinicalObservationVo : clinicalObservationVos) {
			assertThat(clinicalObservationVo)
					.isNotNull();

			assertThat(clinicalObservationVo.getId())
					.isNotNull();
		}
	}


	private List<ClinicalObservationVo> mockVitalSign(String status, LocalDateTime localDateTime){
		List<ClinicalObservationVo> vitalSigns = new ArrayList();
		for (int i=0;i<1;i++){
			ClinicalObservationVo diastolic = new ClinicalObservationVo(i+1, EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), status,"1", localDateTime.plusSeconds(i));
			vitalSigns.add(diastolic);

			ClinicalObservationVo systolic = new ClinicalObservationVo(i+2, EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), status, "11123", localDateTime.plusSeconds(i));
			vitalSigns.add(systolic);

			ClinicalObservationVo temperature = new ClinicalObservationVo(i+3, EVitalSign.TEMPERATURE.getSctidCode(), status,"14", localDateTime.plusSeconds(i));
			vitalSigns.add(temperature);

			ClinicalObservationVo respiratoryRate = new ClinicalObservationVo(i+4, EVitalSign.RESPIRATORY_RATE.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			vitalSigns.add(respiratoryRate);

			ClinicalObservationVo heartRate = new ClinicalObservationVo(i+5, EVitalSign.HEART_RATE.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			vitalSigns.add(heartRate);

			ClinicalObservationVo mean = new ClinicalObservationVo(i+6, EVitalSign.MEAN_PRESSURE.getSctidCode(), status,"1", localDateTime.plusSeconds(i));
			vitalSigns.add(mean);

			ClinicalObservationVo bloodOxygen = new ClinicalObservationVo(i+7, EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(), status, "1", localDateTime.plusSeconds(i));
			vitalSigns.add(bloodOxygen);
		}
		return vitalSigns;
	}

}
