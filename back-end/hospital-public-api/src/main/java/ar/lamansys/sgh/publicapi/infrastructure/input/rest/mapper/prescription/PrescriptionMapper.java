package ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.prescription;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionValidStatesEnum;

import org.springframework.stereotype.Component;

import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import ar.lamansys.sgh.publicapi.domain.prescription.CommercialMedicationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.GenericMedicationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.InstitutionPrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PatientPrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionLineBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProblemBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionProfessionalRegistrationBo;
import ar.lamansys.sgh.publicapi.domain.prescription.ProfessionalPrescriptionBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.CommercialMedicationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.GenericMedicationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.InstitutionPrescriptionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PatientPrescriptionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionLineDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionProblemDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionProfessionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionProfessionalRegistrationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ProfessionalPrescriptionDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@Component
public class PrescriptionMapper {
	private final LocalDateMapper localDateMapper;
	private static final short VENCIDO = 4;
	public PrescriptionMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public PrescriptionDto mapTo(PrescriptionBo prescriptionBo) throws PrescriptionBoException {
		if (prescriptionBo.getPrescriptionId() == null) {
			throw new PrescriptionBoException(PrescriptionBoEnumException.NOT_EXISTS_ID_OR_DNI, "No se encontró información sobre ese dni o id de receta");
		}
		return PrescriptionDto.builder()
				.domain(prescriptionBo.getDomain())
				.prescriptionId(prescriptionBo.getPrescriptionId())
				.dueDate(prescriptionBo.getDueDate())
				.institutionPrescriptionDto(mapTo(prescriptionBo.getInstitutionPrescriptionBo()))
				.prescriptionDate(prescriptionBo.getPrescriptionDate())
				.patientPrescriptionDto(mapTo(prescriptionBo.getPatientPrescriptionBo()))
				.professionalPrescriptionDto(mapTo(prescriptionBo.getProfessionalPrescriptionBo()))
				.prescriptionsLineDto(mapToPrescriptionLineDtoList(prescriptionBo.getPrescriptionsLineBo(), prescriptionBo.getDueDate()))
				.build();
	}

	private List<PrescriptionLineDto> mapToPrescriptionLineDtoList(List<PrescriptionLineBo> prescriptionsLineBo, LocalDateTime dueDate) {
		if(prescriptionsLineBo == null) {
			return new ArrayList<>();
		}
		return prescriptionsLineBo.stream().map(line -> mapTo(line, dueDate)).collect(Collectors.toList());
	}

	private PrescriptionLineDto mapTo(PrescriptionLineBo prescriptionLineBo, LocalDateTime dueDate) {
		var due = localDateMapper.fromLocalDateTime(LocalDateTime.now()).plusDays(30).isAfter(localDateMapper.fromLocalDateTime(dueDate));
		return PrescriptionLineDto.builder()
				.prescriptionLineNumber(prescriptionLineBo.getPrescriptionLineNumber())
				.prescriptionLineStatus(due ? PrescriptionValidStatesEnum.map(VENCIDO).toString() : prescriptionLineBo.getPrescriptionLineStatus())
				.dayDosis(prescriptionLineBo.getDayDosis())
				.presentation(prescriptionLineBo.getPresentation())
				.presentationQuantity(prescriptionLineBo.getPresentationQuantity())
				.duration(prescriptionLineBo.getDuration())
				.unitDosis(prescriptionLineBo.getUnitDosis())
				.prescriptionProblemDto(mapTo(prescriptionLineBo.getPrescriptionProblemBo()))
				.genericMedicationDto(mapTo(prescriptionLineBo.getGenericMedicationBo()))
				.commercialMedicationDto(mapTo(prescriptionLineBo.getCommercialMedicationBo()))
				.build();

	}

	private CommercialMedicationDto mapTo(CommercialMedicationBo commercialMedicationBo) {
		return CommercialMedicationDto.builder()
				.name(commercialMedicationBo.getName())
				.snomedId(commercialMedicationBo.getSnomedId())
				.build();
	}

	private GenericMedicationDto mapTo(GenericMedicationBo genericMedicationBo) {
		return GenericMedicationDto.builder()
				.name(genericMedicationBo.getName())
				.snomedId(genericMedicationBo.getSnomedId())
				.build();
	}

	private PrescriptionProblemDto mapTo(PrescriptionProblemBo prescriptionProblemBo) {
		return PrescriptionProblemDto.builder()
				.pt(prescriptionProblemBo.getPt())
				.problemType(prescriptionProblemBo.getProblemType())
				.snomedId(prescriptionProblemBo.getSnomedId())
				.build();
	}

	private ProfessionalPrescriptionDto mapTo(ProfessionalPrescriptionBo professionalPrescriptionBo) {
		if(professionalPrescriptionBo == null) {
			return new ProfessionalPrescriptionDto();
		}
		return ProfessionalPrescriptionDto.builder()
				.email(professionalPrescriptionBo.getEmail())
				.identificationType(professionalPrescriptionBo.getIdentificationType())
				.phoneNumber(professionalPrescriptionBo.getPhoneNumber())
				.name(professionalPrescriptionBo.getName())
				.lastName(professionalPrescriptionBo.getLastName())
				.identificationNumber(professionalPrescriptionBo.getIdentificationNumber())
				.specialties(mapTo(professionalPrescriptionBo.getSpecialties()))
				.registrations(mapToListPrescriptionProfessionalRegistrationDto(professionalPrescriptionBo.getRegistrations()))
				.build();
	}

	private List<PrescriptionProfessionalRegistrationDto> mapToListPrescriptionProfessionalRegistrationDto(List<PrescriptionProfessionalRegistrationBo> registrations) {
		return registrations.stream().map(this::mapTo).collect(Collectors.toList());
	}

	private PrescriptionProfessionalRegistrationDto mapTo(PrescriptionProfessionalRegistrationBo prescriptionProfessionalRegistrationBo) {
		return PrescriptionProfessionalRegistrationDto.builder()
				.registrationType(prescriptionProfessionalRegistrationBo.getRegistrationType())
				.registrationNumber(prescriptionProfessionalRegistrationBo.getRegistrationNumber())
				.build();
	}

	private List<PrescriptionProfessionDto> mapTo(List<PrescriptionProfessionBo> specialties) {
		return specialties.stream().map(this::mapTo).collect(Collectors.toList());
	}

	private PrescriptionProfessionDto mapTo(PrescriptionProfessionBo prescriptionProfessionBo) {
		return PrescriptionProfessionDto.builder()
				.snomedId(prescriptionProfessionBo.getSnomedId())
				.profession(prescriptionProfessionBo.getProfession())
				.build();
	}

	private PatientPrescriptionDto mapTo(PatientPrescriptionBo patientPrescriptionBo) {
		if(patientPrescriptionBo == null) {
			return new PatientPrescriptionDto();
		}
		return PatientPrescriptionDto.builder()
				.affiliateNumber(patientPrescriptionBo.getAffiliateNumber())
				.dniSex(patientPrescriptionBo.getDniSex())
				.gender(patientPrescriptionBo.getGender())
				.identificationNumber(patientPrescriptionBo.getIdentificationNumber())
				.identificationType(patientPrescriptionBo.getIdentificationType())
				.lastName(patientPrescriptionBo.getLastName())
				.medicalCoverage(patientPrescriptionBo.getMedicalCoverage())
				.medicalCoverageCuit(patientPrescriptionBo.getMedicalCoverageCuit())
				.medicalCoveragePlan(patientPrescriptionBo.getMedicalCoveragePlan())
				.birthDate(patientPrescriptionBo.getBirthDate())
				.selfPerceivedName(patientPrescriptionBo.getSelfPerceivedName())
				.build();
	}

	private InstitutionPrescriptionDto mapTo(InstitutionPrescriptionBo institutionPrescriptionBo) {
		if(institutionPrescriptionBo == null) {
			return new InstitutionPrescriptionDto();
		}
		return InstitutionPrescriptionDto.builder()
				.name(institutionPrescriptionBo.getName() != null ? institutionPrescriptionBo.getName() : null)
				.address(institutionPrescriptionBo.getAddress() != null ? institutionPrescriptionBo.getAddress() : null)
				.provinceCode(institutionPrescriptionBo.getProvinceCode() != null ? institutionPrescriptionBo.getProvinceCode() : null)
				.sisaCode(institutionPrescriptionBo.getSisaCode() != null ? institutionPrescriptionBo.getSisaCode() : null)
				.build();
	}
}
