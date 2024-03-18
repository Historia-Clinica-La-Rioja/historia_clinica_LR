package net.pladema.cipres.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedCipresEncounterPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres.CipresOutpatientConsultationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.application.port.CipresConsultationStorage;

import net.pladema.cipres.domain.AnthropometricDataBo;
import net.pladema.cipres.domain.BasicDataPatientBo;
import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;

import net.pladema.cipres.domain.RiskFactorBo;
import net.pladema.cipres.domain.SnomedBo;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharedCipresEncounterPortImpl implements SharedCipresEncounterPort {

	private final CipresConsultationStorage cipresConsultationStorage;


	public void forwardOutpatientConsultation(CipresOutpatientConsultationDto consultation, Integer cipresEncounterId) {
		log.debug("Input parameter -> consultation {}, cipresEncounterId {} ", consultation, cipresEncounterId);
		cipresConsultationStorage.forwardOutpatientConsultation(mapToOutpatientConsultationBo(consultation), cipresEncounterId);
	}

	private OutpatientConsultationBo mapToOutpatientConsultationBo(CipresOutpatientConsultationDto consultation) {
		return OutpatientConsultationBo.builder()
				.id(consultation.getId())
				.date(consultation.getDate())
				.patient(mapToBasicPatientDataBo(consultation.getPatient()))
				.clinicalSpecialtyId(consultation.getClinicalSpecialtyId())
				.clinicalSpecialtySctid(consultation.getClinicalSpecialtySctid())
				.anthropometricData(mapToAnthropometricDataBo(consultation.getAnthropometricData()))
				.riskFactor(mapToSharedRiskFactorBo(consultation.getRiskFactor()))
				.problems(consultation.getProblems().stream().map(this::mapToSnomedBo).collect(Collectors.toList()))
				.procedures(consultation.getProcedures().stream().map(this::mapToSnomedBo).collect(Collectors.toList()))
				.medications(consultation.getMedications().stream().map(this::mapToSnomedBo).collect(Collectors.toList()))
				.institutionId(consultation.getInstitutionId())
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
				.id(person.getId())
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
		return null;
	}

	public RiskFactorBo mapToSharedRiskFactorBo(SharedRiskFactorDto riskFactorDto) {
		if(riskFactorDto != null)
			return RiskFactorBo.builder()
					.diastolicBloodPressure(riskFactorDto.getDiastolicBloodPressure())
					.systolicBloodPressure(riskFactorDto.getSystolicBloodPressure())
					.build();
		return null;
	}

	public SnomedBo mapToSnomedBo(SharedSnomedDto snomed) {
		return new SnomedBo(snomed.getSctid(), snomed.getPt());
	}

}
