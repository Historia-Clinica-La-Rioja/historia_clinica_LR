package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import net.pladema.clinichistory.ips.repository.ClinicalObservationRepository;
import net.pladema.clinichistory.ips.repository.ObservationLabRepository;
import net.pladema.clinichistory.ips.repository.ObservationVitalSignRepository;
import net.pladema.clinichistory.ips.repository.generalstate.ClinicalObservationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ObservationStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import net.pladema.clinichistory.ips.service.domain.MapClinicalObservationVo;
import net.pladema.clinichistory.ips.service.domain.VitalSignBo;
import net.pladema.clinichistory.ips.service.domain.enums.EVitalSign;
import net.pladema.clinichistory.ips.service.impl.ClinicalObservationServiceImpl;
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
public class ClinicalObservationServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private ClinicalObservationServiceImpl vitalSignLabService;

	@MockBean
	private ObservationVitalSignRepository observationVitalSignRepository;

	@MockBean
	private ClinicalObservationRepository clinicalObservationRepository;

	@MockBean
	private ObservationLabRepository observationLabRepository;

	@MockBean
	private DocumentService documentService;

	@Before
	public void setUp() {
		vitalSignLabService = new ClinicalObservationServiceImpl(observationVitalSignRepository, clinicalObservationRepository,
				observationLabRepository, documentService);
	}


	@Test
	public void test_getVitalSignsGeneralState_complete() {
		Integer internmentEpisodeId = 1;
		int quantity = 2;
		when(clinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
		Last2VitalSignsBo last2VitalSignsBo = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(last2VitalSignsBo)
				.isNotNull();

		assertThat(last2VitalSignsBo.getCurrent())
				.isNotNull();

		assertVitalSignsBo(last2VitalSignsBo.getCurrent());

		assertThat(last2VitalSignsBo.getPrevious())
				.isNotNull();

		assertVitalSignsBo(last2VitalSignsBo.getPrevious());
	}

	@Test
	public void test_getVitalSignsGeneralState_partial() {
		Integer internmentEpisodeId = 1;
		int quantity = 1;
		when(clinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
		Last2VitalSignsBo last2VitalSignsBo = vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(last2VitalSignsBo)
				.isNotNull();

		assertThat(last2VitalSignsBo.getCurrent())
				.isNotNull();

		assertVitalSignsBo(last2VitalSignsBo.getCurrent());

		assertThat(last2VitalSignsBo.getPrevious())
				.isNull();
	}

	@Test
	public void test_getVitalSignsGeneralState_empty() {
		Integer internmentEpisodeId = 1;
		int quantity = 0;
		when(clinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockVitalSignsVo(quantity)));
		Last2VitalSignsBo last2VitalSignsBo =  vitalSignLabService.getLast2VitalSignsGeneralState(internmentEpisodeId);

		assertThat(last2VitalSignsBo)
				.isNotNull();

		assertThat(last2VitalSignsBo.getCurrent())
				.isNull();

		assertThat(last2VitalSignsBo.getPrevious())
				.isNull();
	}

	private void assertVitalSignsBo(VitalSignBo vitalSignBo) {
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
