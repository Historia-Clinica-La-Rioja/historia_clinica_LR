package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.generalstate.VitalSignVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.domain.ips.MapVitalSigns;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MapVitalSignsTest {

	@Before
	public void setUp() {
	}


	@Test
	@Ignore
	public void testMapVitalSigns() {
		List<VitalSignVo> resultQuery = new ArrayList<>();
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now()));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now()));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, LocalDateTime.now()));
		resultQuery.addAll(mockVitalSign(ObservationStatus.ERROR, LocalDateTime.now()));
		resultQuery.addAll(mockVitalSign(ObservationStatus.FINAL, LocalDateTime.now()));

		MapVitalSigns mapVitalSigns = new MapVitalSigns(resultQuery);

		assertThat(mapVitalSigns.getGroupByVitalSign().entrySet())
				.isNotNull()
				.isNotEmpty();
		mapVitalSigns.getGroupByVitalSign().forEach((k, v) -> {

			assertThat(v)
					.isNotNull()
					.isNotEmpty()
					.hasSize(1);

			assertVitalSignsVo(v);
		});

	}

	private void assertVitalSignsVo(List<VitalSignVo> vitalSignVos) {
		for (VitalSignVo vitalSignVo : vitalSignVos) {
			assertThat(vitalSignVo)
					.isNotNull();

			assertThat(vitalSignVo.getId())
					.isNotNull();
		}
	}


	private List<VitalSignVo> mockVitalSign(String status, LocalDateTime localDateTime){
		List<VitalSignVo> vitalSigns = new ArrayList();
		for (int i=0;i<1;i++){
			VitalSignVo diastolic = new VitalSignVo(i+1, EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), status,"1", localDateTime);
			vitalSigns.add(diastolic);

			VitalSignVo sistolic = new VitalSignVo(i+2, EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), status, "11123", localDateTime);
			vitalSigns.add(sistolic);

			VitalSignVo temperature = new VitalSignVo(i+3, EVitalSign.TEMPERATURE.getSctidCode(), status,"14", localDateTime);
			vitalSigns.add(temperature);

			VitalSignVo respiratoryRate = new VitalSignVo(i+4, EVitalSign.RESPIRATORY_RATE.getSctidCode(), status, "1", localDateTime);
			vitalSigns.add(respiratoryRate);

			VitalSignVo heartRate = new VitalSignVo(i+5, EVitalSign.HEART_RATE.getSctidCode(), status, "1", localDateTime);
			vitalSigns.add(heartRate);

			VitalSignVo mean = new VitalSignVo(i+6, EVitalSign.MEAN_PRESSURE.getSctidCode(), status,"1", localDateTime);
			vitalSigns.add(mean);

			VitalSignVo bloodOxygen = new VitalSignVo(i+7, EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(), status, "1", LocalDateTime.now());
			vitalSigns.add(bloodOxygen);
		}
		return vitalSigns;
	}

}
