package net.pladema.clinichistory.cipres.application.forwardoutpatientconsultationtocipres;


import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedCipresEncounterPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres.CipresOutpatientConsultationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.cipres.application.port.CipresOutpatientConsultationStorage;

import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientPatientBo;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForwardOutpatientConsultationToCipres {

	private final CipresOutpatientConsultationStorage cipresOutpatientConsultationStorage;

	private final SharedCipresEncounterPort sharedCipresEncounterPort;


	public void run(Integer cipresEncounterId) {
		log.debug("Input parameter -> cipresEncounterId {} ", cipresEncounterId);
		CipresOutpatientBasicDataBo consultation = cipresOutpatientConsultationStorage.getOutpatientConsultationData(cipresEncounterId);
		CipresOutpatientConsultationDto result = mapToCipresOutpatientConsultationDto(consultation);
		sharedCipresEncounterPort.forwardOutpatientConsultation(result, cipresEncounterId);
	}

	private CipresOutpatientConsultationDto mapToCipresOutpatientConsultationDto(CipresOutpatientBasicDataBo consultation) {
		return CipresOutpatientConsultationDto.builder()
				.id(consultation.getId())
				.anthropometricData(mapToSharedAnthropometricDataDto(consultation.getAnthropometricData()))
				.riskFactor(mapToSharedRiskFactorDto(consultation.getRiskFactorData()))
				.procedures(consultation.getProcedures().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.problems(consultation.getProblems().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.medications(consultation.getMedications().stream().map(m -> new SharedSnomedDto(m.getSctid(), m.getPt())).collect(Collectors.toList()))
				.patient(mapToBasicPatientDto(consultation.getPatient()))
				.date(consultation.getDate().toString())
				.institutionId(consultation.getInstitutionId())
				.institutionSisaCode(consultation.getInstitutionSisaCode())
				.clinicalSpecialtyId(consultation.getClinicalSpecialtyId())
				.clinicalSpecialtySctid(consultation.getClinicalSpecialtySctid())
				.build();
	}

	public BasicPatientDto mapToBasicPatientDto(OutpatientPatientBo outpatientPatientBo) {
		BasicPatientDto result = new BasicPatientDto();
		result.setId(outpatientPatientBo.getId());
		result.setPerson(mapToBasicDataPersonDto(outpatientPatientBo));
		return result;
	}

	public BasicDataPersonDto mapToBasicDataPersonDto(OutpatientPatientBo outpatientPatientBo) {
		BasicDataPersonDto result = new BasicDataPersonDto();
		result.setId(outpatientPatientBo.getPersonId());
		result.setIdentificationTypeId(outpatientPatientBo.getIdentificationType());
		result.setIdentificationNumber(outpatientPatientBo.getIdentificationNumber());
		GenderDto gender = new GenderDto();
		gender.setId(outpatientPatientBo.getGenderId());
		result.setGender(gender);
		return result;
	}

	public SharedRiskFactorDto mapToSharedRiskFactorDto(RiskFactorBo riskFactors) {
		if (riskFactors != null) {
			return SharedRiskFactorDto.builder()
					.diastolicBloodPressure(riskFactors.getDiastolicBloodPressure() != null ? riskFactors.getDiastolicBloodPressure().getValue() : null)
					.systolicBloodPressure(riskFactors.getSystolicBloodPressure() != null ? riskFactors.getSystolicBloodPressure().getValue() : null)
					.build();
		}
		return null;
	}

	private SharedAnthropometricDataDto mapToSharedAnthropometricDataDto(AnthropometricDataBo anthropometricData) {
		if (anthropometricData != null) {
			return SharedAnthropometricDataDto.builder()
					.bloodType(anthropometricData.getBloodType() != null ? anthropometricData.getBloodType().getValue() : null)
					.height(anthropometricData.getHeight() != null ? anthropometricData.getHeight().getValue() : null)
					.weight(anthropometricData.getWeight() != null ? anthropometricData.getWeight().getValue() : null)
					.bmi(anthropometricData.getBMI() != null && !anthropometricData.getBMI().getValue().equals("-") ? anthropometricData.getBMI().getValue() : null)
					.headCircumference(anthropometricData.getHeadCircumference() != null ? anthropometricData.getHeadCircumference().getValue() : null)
					.build();
		}
		return null;
	}

}
