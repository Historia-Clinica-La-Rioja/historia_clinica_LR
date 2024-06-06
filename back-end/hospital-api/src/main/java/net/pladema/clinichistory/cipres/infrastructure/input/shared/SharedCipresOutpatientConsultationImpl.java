package net.pladema.clinichistory.cipres.infrastructure.input.shared;

import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres.CipresOutpatientConsultationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedCipresOutpatientConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.cipres.application.port.CipresOutpatientConsultationStorage;

import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientPatientBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedCipresOutpatientConsultationImpl implements SharedCipresOutpatientConsultationPort {

	private final CipresOutpatientConsultationStorage cipresOutpatientConsultationStorage;

	@Override
	public List<CipresOutpatientConsultationDto> getOutpatientConsultations() {
		log.debug("fetch consultations to create");
		List<CipresOutpatientBasicDataBo> consultations = cipresOutpatientConsultationStorage.getOutpatientConsultations();
		List<CipresOutpatientConsultationDto> result = consultations
				.stream()
				.map(this::mapToOutpatientConsultationDto)
				.collect(Collectors.toList());
		log.debug("Output size -> {}", result.size());
		return result;
	}

	public CipresOutpatientConsultationDto mapToOutpatientConsultationDto(CipresOutpatientBasicDataBo oc) {
		return  CipresOutpatientConsultationDto.builder()
				.id(oc.getId())
				.date(oc.getDate().toString())
				.patient(mapToBasicPatientDto(oc.getPatient()))
				.institutionId(oc.getInstitutionId())
				.institutionSisaCode(oc.getInstitutionSisaCode())
				.clinicalSpecialtyId(oc.getClinicalSpecialtyId())
				.clinicalSpecialtySctid(oc.getClinicalSpecialtySctid())
				.anthropometricData(mapToAnthropometricDataDto(oc.getAnthropometricData()))
				.riskFactor(mapToSharedRiskFactorDto(oc.getRiskFactorData()))
				.procedures(oc.getProcedures().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.problems(oc.getProblems().stream().map(p -> new SharedSnomedDto(p.getSctid(), p.getPt())).collect(Collectors.toList()))
				.medications(oc.getMedications().stream().map(m -> new SharedSnomedDto(m.getSctid(), m.getPt())).collect(Collectors.toList()))
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

	public SharedAnthropometricDataDto mapToAnthropometricDataDto(AnthropometricDataBo anthropometricDataBo) {
		if (anthropometricDataBo != null)
			return SharedAnthropometricDataDto.builder()
					.bloodType(anthropometricDataBo.getBloodType() != null ? anthropometricDataBo.getBloodType().getValue() : null)
					.height(anthropometricDataBo.getHeight() != null ? anthropometricDataBo.getHeight().getValue() : null)
					.weight(anthropometricDataBo.getWeight() != null ? anthropometricDataBo.getWeight().getValue() : null)
					.bmi(anthropometricDataBo.getBMI() != null && !anthropometricDataBo.getBMI().getValue().equals("-") ? anthropometricDataBo.getBMI().getValue() : null)
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