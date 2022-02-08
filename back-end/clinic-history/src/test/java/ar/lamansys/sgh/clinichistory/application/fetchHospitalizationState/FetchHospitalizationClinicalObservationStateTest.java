package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import ar.lamansys.sgh.clinichistory.domain.ips.Last2RiskFactorsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHClinicalObservationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchHospitalizationClinicalObservationStateTest {

	private FetchHospitalizationClinicalObservationState clinicalObservationGeneralStateService;

	@Mock
	private HCHClinicalObservationRepository hchClinicalObservationRepository;

	@BeforeEach
	void setUp() {
		clinicalObservationGeneralStateService = new FetchHospitalizationClinicalObservationState(hchClinicalObservationRepository);
	}


	@Test
	void test_getRiskFactorsGeneralState_complete() {
		Integer internmentEpisodeId = 1;
		int quantity = 2;
		when(hchClinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockRiskFactorsVo(quantity)));
		Last2RiskFactorsBo last2RiskFactorsBo = clinicalObservationGeneralStateService.getLast2RiskFactorsGeneralState(internmentEpisodeId);

		assertThat(last2RiskFactorsBo)
				.isNotNull();

		assertThat(last2RiskFactorsBo.getCurrent())
				.isNotNull();

		assertRiskFactorsBo(last2RiskFactorsBo.getCurrent());

		assertThat(last2RiskFactorsBo.getPrevious())
				.isNotNull();

		assertRiskFactorsBo(last2RiskFactorsBo.getPrevious());
	}

	@Test
	void test_getRiskFactorsGeneralState_partial() {
		Integer internmentEpisodeId = 1;
		int quantity = 1;
		when(hchClinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockRiskFactorsVo(quantity)));
		Last2RiskFactorsBo last2RiskFactorsBo = clinicalObservationGeneralStateService.getLast2RiskFactorsGeneralState(internmentEpisodeId);

		assertThat(last2RiskFactorsBo)
				.isNotNull();

		assertThat(last2RiskFactorsBo.getCurrent())
				.isNotNull();

		assertRiskFactorsBo(last2RiskFactorsBo.getCurrent());

		assertThat(last2RiskFactorsBo.getPrevious())
				.isNull();
	}

	@Test
	void test_getRiskFactorsGeneralState_empty() {
		Integer internmentEpisodeId = 1;
		int quantity = 0;
		when(hchClinicalObservationRepository.getGeneralState(internmentEpisodeId))
				.thenReturn(new MapClinicalObservationVo(mockRiskFactorsVo(quantity)));
		Last2RiskFactorsBo last2RiskFactorsBo =  clinicalObservationGeneralStateService.getLast2RiskFactorsGeneralState(internmentEpisodeId);

		assertThat(last2RiskFactorsBo)
				.isNotNull();

		assertThat(last2RiskFactorsBo.getCurrent())
				.isNull();

		assertThat(last2RiskFactorsBo.getPrevious())
				.isNull();
	}

	private void assertRiskFactorsBo(RiskFactorBo riskFactorBo) {
		assertThat(riskFactorBo)
				.isNotNull();

		assertThat(riskFactorBo.hasValues())
				.isTrue();

		assertThat(riskFactorBo.getBloodOxygenSaturation())
				.isNotNull();

		assertThat(riskFactorBo.getTemperature())
				.isNotNull();

		assertThat(riskFactorBo.getSystolicBloodPressure())
				.isNotNull();

		assertThat(riskFactorBo.getDiastolicBloodPressure())
				.isNotNull();

		assertThat(riskFactorBo.getMeanPressure())
				.isNotNull();

		assertThat(riskFactorBo.getHeartRate())
				.isNotNull();

		assertThat(riskFactorBo.getRespiratoryRate())
				.isNotNull();
	}


	private List<ClinicalObservationVo> mockRiskFactorsVo(int quantity){
		List<ClinicalObservationVo> riskFactors = new ArrayList<>();
		for (int i=0;i<quantity;i++){
			ClinicalObservationVo diastolic = new ClinicalObservationVo(i+1, ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			riskFactors.add(diastolic);

			ClinicalObservationVo systolic = new ClinicalObservationVo(i+2, ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getSctidCode(), ObservationStatus.FINAL, "11123", LocalDateTime.now());
			riskFactors.add(systolic);

			ClinicalObservationVo temperature = new ClinicalObservationVo(i+3, ERiskFactor.TEMPERATURE.getSctidCode(), ObservationStatus.FINAL,"14", LocalDateTime.now());
			riskFactors.add(temperature);

			ClinicalObservationVo respiratoryRate = new ClinicalObservationVo(i+4, ERiskFactor.RESPIRATORY_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			riskFactors.add(respiratoryRate);

			ClinicalObservationVo heartRate = new ClinicalObservationVo(i+5, ERiskFactor.HEART_RATE.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			riskFactors.add(heartRate);

			ClinicalObservationVo mean = new ClinicalObservationVo(i+6, ERiskFactor.MEAN_PRESSURE.getSctidCode(), ObservationStatus.FINAL,"1", LocalDateTime.now());
			riskFactors.add(mean);

			ClinicalObservationVo bloodOxygen = new ClinicalObservationVo(i+7, ERiskFactor.BLOOD_OXYGEN_SATURATION.getSctidCode(), ObservationStatus.FINAL, "1", LocalDateTime.now());
			riskFactors.add(bloodOxygen);
		}
		return riskFactors;
	}

}
