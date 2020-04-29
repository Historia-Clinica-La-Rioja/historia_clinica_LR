package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.ObservationLabRepository;
import net.pladema.internation.repository.ips.ObservationVitalSignRepository;
import net.pladema.internation.repository.ips.generalstate.VitalSignVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.domain.ips.MapVitalSigns;
import net.pladema.internation.service.domain.ips.VitalSignBo;
import net.pladema.internation.service.domain.ips.enums.EVitalSign;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class VitalSignLabServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private VitalSignLabServiceImpl vitalSignLabService;

	@MockBean
	private ObservationVitalSignRepository observationVitalSignRepository;

	@MockBean
	private ObservationLabRepository observationLabRepository;

	@MockBean
	private DocumentService documentService;

	@Before
	public void setUp() {
		vitalSignLabService = new VitalSignLabServiceImpl(observationVitalSignRepository,
				observationLabRepository, documentService);
	}


	@Test
	public void test_getVitalSignsGeneralState_complete() {
		Integer internmentEpisodeId = 1;
		int quantity = 2;
		when(observationVitalSignRepository.getVitalSignsGeneralStateLastSevenDays(internmentEpisodeId))
				.thenReturn(new MapVitalSigns(mockVitalSignsVo(quantity)));
		List<VitalSignBo> vitalSignBos = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(vitalSignBos)
				.isNotNull()
				.hasSize(quantity);

		assertVitalSignsBo(vitalSignBos);
	}

	@Test
	public void test_getVitalSignsGeneralState_partial() {
		Integer internmentEpisodeId = 1;
		int quantity = 1;
		when(observationVitalSignRepository.getVitalSignsGeneralStateLastSevenDays(internmentEpisodeId))
				.thenReturn(new MapVitalSigns(mockVitalSignsVo(quantity)));
		List<VitalSignBo> vitalSignBos = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(vitalSignBos)
				.isNotNull()
				.hasSize(quantity);

		assertVitalSignsBo(vitalSignBos);
	}

	@Test
	public void test_getVitalSignsGeneralState_empty() {
		Integer internmentEpisodeId = 1;
		int quantity = 0;
		when(observationVitalSignRepository.getVitalSignsGeneralStateLastSevenDays(internmentEpisodeId))
				.thenReturn(new MapVitalSigns(mockVitalSignsVo(quantity)));
		List<VitalSignBo> vitalSignBos = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(vitalSignBos)
				.isNotNull()
				.hasSize(quantity);
	}

	private void assertVitalSignsBo(List<VitalSignBo> vitalSignBos) {
		for (VitalSignBo vitalSignBo : vitalSignBos) {
			assertThat(vitalSignBo)
					.isNotNull();

			assertThat(vitalSignBo.hasValues())
					.isTrue();

			assertThat(vitalSignBo.getBloodOxygenSaturation())
					.isNotNull();

			assertThat(vitalSignBo.getTemperature())
					.isNotNull();

			assertThat(vitalSignBo.getSystolicBloodPressure())
					.isNotNull();

			assertThat(vitalSignBo.getDiastolicBloodPressure())
					.isNotNull();

			assertThat(vitalSignBo.getMeanPressure())
					.isNotNull();

			assertThat(vitalSignBo.getHeartRate())
					.isNotNull();

			assertThat(vitalSignBo.getRespiratoryRate())
					.isNotNull();
		}
	}


	private List<VitalSignVo> mockVitalSignsVo(int quantity){
		List<VitalSignVo> vitalSigns = new ArrayList();
		for (int i=0;i<quantity;i++){
			VitalSignVo diastolic = new VitalSignVo(i+1, EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			vitalSigns.add(diastolic);

			VitalSignVo sistolic = new VitalSignVo(i+2, EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL, "11123", LocalDateTime.now());
			vitalSigns.add(sistolic);

			VitalSignVo temperature = new VitalSignVo(i+3, EVitalSign.TEMPERATURE.getSctidCode(), ObservationStatus.FINAL,"14", LocalDateTime.now());
			vitalSigns.add(temperature);

			VitalSignVo respiratoryRate = new VitalSignVo(i+4, EVitalSign.RESPIRATORY_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(respiratoryRate);

			VitalSignVo heartRate = new VitalSignVo(i+5, EVitalSign.HEART_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(heartRate);

			VitalSignVo mean = new VitalSignVo(i+6, EVitalSign.MEAN_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			vitalSigns.add(mean);

			VitalSignVo bloodOxygen = new VitalSignVo(i+7, EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(bloodOxygen);
		}
		return vitalSigns;
	}

}
