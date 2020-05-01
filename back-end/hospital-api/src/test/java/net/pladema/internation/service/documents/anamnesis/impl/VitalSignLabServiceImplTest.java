package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.ObservationLabRepository;
import net.pladema.internation.repository.ips.ObservationVitalSignRepository;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;
import net.pladema.internation.repository.masterdata.entity.ObservationStatus;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.domain.ips.MapClinicalObservationVo;
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
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
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
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
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
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
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


	private List<ClinicalObservationVo> mockVitalSignsVo(int quantity){
		List<ClinicalObservationVo> vitalSigns = new ArrayList();
		for (int i=0;i<quantity;i++){
			ClinicalObservationVo diastolic = new ClinicalObservationVo(i+1, EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			vitalSigns.add(diastolic);

			ClinicalObservationVo sistolic = new ClinicalObservationVo(i+2, EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL, "11123", LocalDateTime.now());
			vitalSigns.add(sistolic);

			ClinicalObservationVo temperature = new ClinicalObservationVo(i+3, EVitalSign.TEMPERATURE.getSctidCode(), ObservationStatus.FINAL,"14", LocalDateTime.now());
			vitalSigns.add(temperature);

			ClinicalObservationVo respiratoryRate = new ClinicalObservationVo(i+4, EVitalSign.RESPIRATORY_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(respiratoryRate);

			ClinicalObservationVo heartRate = new ClinicalObservationVo(i+5, EVitalSign.HEART_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(heartRate);

			ClinicalObservationVo mean = new ClinicalObservationVo(i+6, EVitalSign.MEAN_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			vitalSigns.add(mean);

			ClinicalObservationVo bloodOxygen = new ClinicalObservationVo(i+7, EVitalSign.BLOOD_OXYGEN_SATURATION.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			vitalSigns.add(bloodOxygen);
		}
		return vitalSigns;
	}

}
