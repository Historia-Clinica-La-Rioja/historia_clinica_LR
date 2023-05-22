package net.pladema.cipres.application.getconsultations;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OutpatientConsultationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedOutpatientConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.domain.AnthropometricDataBo;
import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;

import net.pladema.cipres.domain.RiskFactorBo;
import net.pladema.cipres.domain.SnomedBo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetConsultations {

	private final SharedOutpatientConsultationPort sharedOutpatientConsultationPort;

	public Map<Integer, List<OutpatientConsultationBo>> run() {
		return mapToBo(sharedOutpatientConsultationPort.getOutpatientConsultationsToCipres());
	}

	private Map<Integer, List<OutpatientConsultationBo>> mapToBo(Map<Integer, List<OutpatientConsultationDto>> consultations) {
		Map<Integer, List<OutpatientConsultationBo>> result = new HashMap<>();
		consultations.keySet().forEach(patientId -> {
			result.put(patientId, consultations.get(patientId).stream().map(this::mapToOutpatientConsultationBo).collect(Collectors.toList()));
		});
		return result;
	}

	private OutpatientConsultationBo mapToOutpatientConsultationBo(OutpatientConsultationDto consultation) {
		 return OutpatientConsultationBo.builder()
				 .id(consultation.getId())
				 .date(consultation.getDate())
				 .patient(mapToBasicPatientDataBo(consultation.getPatient()))
				 .clinicalSpecialtySctid(consultation.getClinicalSpecialtySctid())
				 .anthropometricData(mapToAnthropometricDataBo(consultation.getAnthropometricData()))
				 .riskFactor(mapToSharedRiskFactorBo(consultation.getRiskFactor()))
				 .problems(consultation.getProblems().stream().map(this::mapToSnomedBo).collect(Collectors.toList()))
				 .procedures(consultation.getProcedures().stream().map(this::mapToSnomedBo).collect(Collectors.toList()))
				 .institutionSisaCode(consultation.getInstitutionSisaCode())
				 .build();
	}

	private BasicDataPatientBo mapToBasicPatientDataBo(BasicPatientDto patient) {
		return BasicDataPatientBo.builder()
				.id(patient.getId())
				.person(mapToBasicPersonDataBo(patient.getPerson()))
				.build();
	}

	private BasicDataPersonBo mapToBasicPersonDataBo(BasicDataPersonDto person) {
		return BasicDataPersonBo.builder()
				.firstName(person.getFirstName())
				.middleNames(person.getMiddleNames())
				.otherLastNames(person.getOtherLastNames())
				.lastName(person.getLastName())
				.birthDate(person.getBirthDate())
				.identificationNumber(person.getIdentificationNumber())
				.identificationTypeId(person.getIdentificationTypeId())
				.genderId(person.getGender().getId())
				.build();
	}

	public AnthropometricDataBo mapToAnthropometricDataBo(SharedAnthropometricDataDto anthropometricDataDto) {
		if (anthropometricDataDto != null)
			return AnthropometricDataBo.builder()
					.bloodType(anthropometricDataDto.getBloodType())
					.height(anthropometricDataDto.getHeight())
					.weight(anthropometricDataDto.getWeight())
					.bmi(anthropometricDataDto.getBmi())
					.headCircumference(anthropometricDataDto.getHeadCircumference())
					.build();
		else
			return null;
	}

	public RiskFactorBo mapToSharedRiskFactorBo(SharedRiskFactorDto riskFactorDto) {
		if(riskFactorDto != null)
			return RiskFactorBo.builder()
					.diastolicBloodPressure(riskFactorDto.getDiastolicBloodPressure())
					.systolicBloodPressure(riskFactorDto.getSystolicBloodPressure())
					.build();
		else
			return null;
	}

	public SnomedBo mapToSnomedBo(SharedSnomedDto snomed) {
		return new SnomedBo(snomed.getSctid(), snomed.getPt());
	}

}
