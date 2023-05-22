package net.pladema.clinichistory.outpatient.infrastructure.input.shared;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OutpatientConsultationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedOutpatientConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.port.OutpatientConsultationStorage;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.PatientBo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedOutpatientConsultationImpl implements SharedOutpatientConsultationPort {

	private final OutpatientConsultationStorage outpatientConsultationStorage;

	@Override
	public Map<Integer, List<OutpatientConsultationDto>> getOutpatientConsultationsToCipres() {
		log.debug("fetch consultations to create");
		Map<Integer, List<OutpatientBasicDataBo>> consultations = outpatientConsultationStorage.getOutpatientConsultationsToCipres();
		Map<Integer, List<OutpatientConsultationDto>> result = new HashMap<>();
		consultations.entrySet().forEach(consultation -> {
			result.put(
					consultation.getKey(),
					consultation.getValue().stream()
							.map(this::mapToOutpatientConsultationDto)
							.collect(Collectors.toList()));
		});
		log.debug("Output size -> {}", result.size());
		return result;
	}

	public OutpatientConsultationDto mapToOutpatientConsultationDto (OutpatientBasicDataBo oc) {
		return  OutpatientConsultationDto.builder()
				.id(oc.getId())
				.date(oc.getDate().toString())
				.patient(mapToBasicPatientDto(oc.getPatient()))
				.institutionSisaCode(oc.getInstitutionSisaCode())
				.clinicalSpecialtySctid(oc.getClinicalSpecialtySctid())
				.anthropometricData(mapToAnthropometricDataDto(oc.getAnthropometricData()))
				.riskFactor(mapToSharedRiskFactorDto(oc.getRiskFactorData()))
				.procedures(oc.getProcedures().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.problems(oc.getProblems().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.build();
	}

	public BasicPatientDto mapToBasicPatientDto(PatientBo patientBo) {
		BasicPatientDto result = new BasicPatientDto();
		result.setId(patientBo.getId());
		result.setPerson(mapToBasicDataPersonDto(patientBo));
		return result;
	}

	public BasicDataPersonDto mapToBasicDataPersonDto(PatientBo patientBo) {
		BasicDataPersonDto result = new BasicDataPersonDto();
		result.setFirstName(patientBo.getFirstName());
		result.setMiddleNames(patientBo.getMiddleNames());
		result.setLastName(patientBo.getLastName());
		result.setOtherLastNames(patientBo.getOtherLastNames());
		result.setIdentificationTypeId(patientBo.getIdentificationType());
		result.setIdentificationNumber(patientBo.getIdentificationNumber());
		result.setBirthDate(patientBo.getBirthDate());
		GenderDto gender = new GenderDto();
		gender.setId(patientBo.getGenderId());
		result.setGender(gender);
		return result;
	}

	public SharedAnthropometricDataDto mapToAnthropometricDataDto(AnthropometricDataBo anthropometricDataBo) {
		if (anthropometricDataBo != null)
			return SharedAnthropometricDataDto.builder()
					.bloodType(anthropometricDataBo.getBloodType() != null ? anthropometricDataBo.getBloodType().getValue() : null)
					.height(anthropometricDataBo.getHeight() != null ? anthropometricDataBo.getHeight().getValue() : null)
					.weight(anthropometricDataBo.getWeight() != null ? anthropometricDataBo.getWeight().getValue() : null)
					.bmi(anthropometricDataBo.getBMI() != null ? anthropometricDataBo.getBMI().getValue() : null)
					.headCircumference(anthropometricDataBo.getHeadCircumference() != null ? anthropometricDataBo.getHeadCircumference().getValue() : null)
					.build();
		else
			return null;
	}

	public SharedRiskFactorDto mapToSharedRiskFactorDto(RiskFactorBo riskFactorBo) {
		if(riskFactorBo != null)
			return SharedRiskFactorDto.builder()
					.diastolicBloodPressure(riskFactorBo.getDiastolicBloodPressure() != null ? riskFactorBo.getDiastolicBloodPressure().getValue() : null)
					.systolicBloodPressure(riskFactorBo.getSystolicBloodPressure() != null ? riskFactorBo.getSystolicBloodPressure().getValue() : null)
					.build();
		else
			return null;
	}

}