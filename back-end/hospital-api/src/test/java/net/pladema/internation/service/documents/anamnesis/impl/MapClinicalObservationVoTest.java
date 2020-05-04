package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.domain.ips.MapClinicalObservationVo;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
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
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now().plusMinutes(1)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now().plusMinutes(2)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, LocalDateTime.now().plusMinutes(3)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, LocalDateTime.now().plusMinutes(4)));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now().plusMinutes(5)));

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
			ClinicalObservationVo diastolic = new ClinicalObservationVo(i+1, EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), status,"1", localDateTime);
			vitalSigns.add(diastolic);

			ClinicalObservationVo sistolic = new ClinicalObservationVo(i+2, EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), status, "11123", localDateTime);
			vitalSigns.add(sistolic);

			ClinicalObservationVo temperature = new ClinicalObservationVo(i+3, EVitalSign.TEMPERATURE.getSctidCode(), status,"14", localDateTime);
			vitalSigns.add(temperature);

			ClinicalObservationVo respiratoryRate = new ClinicalObservationVo(i+4, EVitalSign.RESPIRATORY_RATE.getSctidCode(), status, "1", localDateTime);
			vitalSigns.add(respiratoryRate);

			ClinicalObservationVo heartRate = new ClinicalObservationVo(i+5, EVitalSign.HEART_RATE.getSctidCode(), status, "1", localDateTime);
			vitalSigns.add(heartRate);

			ClinicalObservationVo mean = new ClinicalObservationVo(i+6, EVitalSign.MEAN_PRESSURE.getSctidCode(), status,"1", localDateTime);
			vitalSigns.add(mean);

			ClinicalObservationVo bloodOxygen = new ClinicalObservationVo(i+7, EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(), status, "1", LocalDateTime.now());
			vitalSigns.add(bloodOxygen);
		}
		return vitalSigns;
	}

}
