package net.pladema.emergencycare.triage.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.ERiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.RiskFactorExternalService;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.triage.controller.dto.TriageBreathingDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCirculationDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.infrastructure.input.rest.mapper.TriageMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.medicalconsultation.doctorsoffice.controller.service.DoctorsOfficeExternalService;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.service.UserPersonExternalService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TriageListMapper {

	private TriageMapper triageMapper;

	private UserPersonExternalService userPersonExternalService;

	private EmergencyCareMapper emergencyCareMapper;

	private DoctorsOfficeExternalService doctorsOfficeExternalService;

	private TriageMasterDataService triageMasterDataService;

	private TriageMasterDataMapper triageMasterDataMapper;

	private RiskFactorExternalService riskFactorExternalService;
	private OutpatientConsultationMapper outpatientConsultationMapper;

	public TriageListDto toTriageListDto(TriageBo triageBo) {
		TriageListDto result = triageMapper.toTriageListDto(triageBo);
		// set user data
		UserDto userDto = userPersonExternalService.getUser(triageBo.getCreatedBy()).get();
		result.setCreatedBy(emergencyCareMapper.toEmergencyCareUserDto(userDto));
		// set doctor's office data
		if (triageBo.getDoctorsOfficeId() != null)
			result.setDoctorsOffice(doctorsOfficeExternalService.getDoctorsOfficeById(triageBo.getDoctorsOfficeId()));
		// set triage category
		TriageCategoryBo category = triageMasterDataService.getCategoryById(triageBo.getCategoryId());
		result.setCategory(triageMasterDataMapper.toTriageCategoryDto(category));
		// set risk factors data
		triageBo.getRiskFactorIds().forEach(riskFactor -> {
			RiskFactorObservationDto riskFactorObservationDto = riskFactorExternalService.getRiskFactorObservationById(riskFactor);
			if (triageBo.isAdultGynecological())
				setRiskFactorAsAdultGynecological(result, riskFactorObservationDto);
			else if (triageBo.isPediatric())
				setRiskFactorAsPediatric(result, riskFactorObservationDto);
		});
		List<ReasonBo> reasons = triageBo.getReasons();
		if(reasons != null && !reasons.isEmpty())
			result.setReasons(outpatientConsultationMapper.toOutpatientReasonDto(reasons));
		return result;
	}

	private void setRiskFactorAsAdultGynecological(TriageListDto triageListDto, RiskFactorObservationDto riskFactorObservationDto) {
		if (triageListDto.getRiskFactors() == null)
			triageListDto.setRiskFactors(new NewRiskFactorsObservationDto());

		if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
			triageListDto.getRiskFactors().setBloodOxygenSaturation(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.HEART_RATE.getLoincCode())) {
			triageListDto.getRiskFactors().setHeartRate(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.RESPIRATORY_RATE.getLoincCode())) {
			triageListDto.getRiskFactors().setRespiratoryRate(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.TEMPERATURE.getLoincCode())) {
			triageListDto.getRiskFactors().setTemperature(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getLoincCode())) {
			triageListDto.getRiskFactors().setSystolicBloodPressure(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getLoincCode())) {
			triageListDto.getRiskFactors().setDiastolicBloodPressure(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_GLUCOSE.getLoincCode())){
			triageListDto.getRiskFactors().setBloodGlucose(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.GLYCOSYLATED_HEMOGLOBIN.getLoincCode())){
			triageListDto.getRiskFactors().setGlycosylatedHemoglobin(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.CARDIOVASCULAR_RISK.getLoincCode())) {
			triageListDto.getRiskFactors().setCardiovascularRisk(riskFactorObservationDto.getRiskFactorObservation());
		}

	}

	private void setRiskFactorAsPediatric(TriageListDto triageListDto, RiskFactorObservationDto riskFactorObservationDto) {
		if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
			if (triageListDto.getBreathing() == null)
				triageListDto.setBreathing(new TriageBreathingDto());
			triageListDto.getBreathing().setBloodOxygenSaturation(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.HEART_RATE.getLoincCode())) {
			if (triageListDto.getCirculation() == null)
				triageListDto.setCirculation(new TriageCirculationDto());
			triageListDto.getCirculation().setHeartRate(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.RESPIRATORY_RATE.getLoincCode())) {
			if (triageListDto.getBreathing() == null)
				triageListDto.setBreathing(new TriageBreathingDto());
			triageListDto.getBreathing().setRespiratoryRate(riskFactorObservationDto.getRiskFactorObservation());
		}
	}

}
