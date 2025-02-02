package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.patient.domain.PatientPrescriptionAddressBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.dto.PatientPrescriptionAddressDto;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionV2Bo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionLineV2Bo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionDosageBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionSpecialtyBo;
import ar.lamansys.sgh.publicapi.prescription.domain.SuggestedCommercialMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionLineV3Dto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionV2Dto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionLineV2Dto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDosageDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionSpecialtyDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionV3Dto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionsDataDto;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionValidStatesEnum;

import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ChangePrescriptionStateMultipleMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.DispensedMedicationBo;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMultipleDto;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMultipleMedicationDto;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.DispensedMedicationDto;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.SuggestedCommercialMedicationDto;

import org.springframework.stereotype.Component;

import ar.lamansys.sgh.publicapi.prescription.domain.CommercialMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.GenericMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.InstitutionPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PatientPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionLineBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProblemBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionalRegistrationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ProfessionalPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.CommercialMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.GenericMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.InstitutionPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PatientPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionLineDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProblemDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionalRegistrationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ProfessionalPrescriptionDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@Component
public class PrescriptionMapper {
	private final LocalDateMapper localDateMapper;
	private static final short VENCIDO = 4;
	public PrescriptionMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public PrescriptionDto mapTo(PrescriptionBo prescriptionBo) throws PrescriptionNotFoundException {
		if (prescriptionBo.getPrescriptionId() == null) {
			throw new PrescriptionNotFoundException("La receta no existe");
		}
		return PrescriptionDto.builder()
				.domain(prescriptionBo.getDomain())
				.prescriptionId(prescriptionBo.getPrescriptionId())
				.dueDate(prescriptionBo.getDueDate())
				.link(prescriptionBo.getLink())
				.isArchived(prescriptionBo.getIsArchived())
				.institutionPrescriptionDto(mapTo(prescriptionBo.getInstitutionPrescriptionBo()))
				.prescriptionDate(prescriptionBo.getPrescriptionDate())
				.patientPrescriptionDto(mapTo(prescriptionBo.getPatientPrescriptionBo()))
				.professionalPrescriptionDto(mapTo(prescriptionBo.getProfessionalPrescriptionBo()))
				.prescriptionsLineDto(mapToPrescriptionLineDtoList(prescriptionBo.getPrescriptionsLineBo(), prescriptionBo.getDueDate()))
				.build();
	}

	public PrescriptionsDataDto mapToPrescriptionsDataDto(PrescriptionsDataBo prescriptionsDataBo){
		return PrescriptionsDataDto.builder()
				.domain(prescriptionsDataBo.getDomain())
				.prescriptionId(prescriptionsDataBo.getPrescriptionId())
				.prescriptionDate(prescriptionsDataBo.getPrescriptionDate())
				.dueDate(prescriptionsDataBo.getDueDate())
				.link(prescriptionsDataBo.getLink())
				.professionalData(mapToProfessionalDataDto(prescriptionsDataBo.getProfessionalData()))
				.prescriptionSpecialty(mapToSpecialtyDto(prescriptionsDataBo.getPrescriptionSpecialty()))
				.patientAddressData(mapToPatientAddressDataDto(prescriptionsDataBo.getPatientAddressData()))
				.build();
	}

	private PatientPrescriptionAddressDto mapToPatientAddressDataDto(PatientPrescriptionAddressBo patientPrescriptionAddressBo){
		return PatientPrescriptionAddressDto.builder()
				.country(patientPrescriptionAddressBo.getCountry())
				.province(patientPrescriptionAddressBo.getProvince())
				.department(patientPrescriptionAddressBo.getDepartment())
				.city(patientPrescriptionAddressBo.getCity())
				.street(patientPrescriptionAddressBo.getStreet())
				.streetNumber(patientPrescriptionAddressBo.getStreetNumber())
				.build();
	}

	private PrescriptionSpecialtyDto mapToSpecialtyDto(PrescriptionSpecialtyBo prescriptionSpecialtyBo){
		return PrescriptionSpecialtyDto.builder()
				.specialty(prescriptionSpecialtyBo.getSpecialty())
				.snomedId(prescriptionSpecialtyBo.getSnomedId())
				.build();
	}

	private ProfessionalPrescriptionDto mapToProfessionalDataDto(ProfessionalPrescriptionBo professionalPrescriptionBo){
		return ProfessionalPrescriptionDto.builder()
				.name(professionalPrescriptionBo.getName())
				.lastName(professionalPrescriptionBo.getLastName())
				.identificationType(professionalPrescriptionBo.getIdentificationType())
				.identificationNumber(professionalPrescriptionBo.getIdentificationNumber())
				.phoneNumber(professionalPrescriptionBo.getPhoneNumber())
				.email(professionalPrescriptionBo.getEmail())
				.professions(mapToProfessionsDto(professionalPrescriptionBo.getProfessions()))
				.registrations(mapToRegistrationsDto(professionalPrescriptionBo.getRegistrations()))
				.build();
	}

	private List<PrescriptionProfessionalRegistrationDto> mapToRegistrationsDto(List<PrescriptionProfessionalRegistrationBo> prescriptionProfessionalRegistrationBo){
		List<PrescriptionProfessionalRegistrationDto> registrations = new ArrayList<>();

		for(PrescriptionProfessionalRegistrationBo bo : prescriptionProfessionalRegistrationBo){
			PrescriptionProfessionalRegistrationDto dto = PrescriptionProfessionalRegistrationDto.builder()
					.registrationNumber(bo.getRegistrationNumber())
					.registrationType(bo.getRegistrationType())
					.build();
			registrations.add(dto);
		}
		return registrations;
	}

	private List<PrescriptionProfessionDto> mapToProfessionsDto(List<PrescriptionProfessionBo> prescriptionProfessionBo){
		List<PrescriptionProfessionDto> professions = new ArrayList<>();

		for (PrescriptionProfessionBo bo : prescriptionProfessionBo) {
			PrescriptionProfessionDto dto = PrescriptionProfessionDto.builder()
					.profession(bo.getProfession())
					.snomedId(bo.getSnomedId())
					.build();
			professions.add(dto);
		}

		return professions;
	}

	private List<PrescriptionLineDto> mapToPrescriptionLineDtoList(List<PrescriptionLineBo> prescriptionsLineBo, LocalDateTime dueDate) {
		if(prescriptionsLineBo == null) {
			return new ArrayList<>();
		}
		return prescriptionsLineBo.stream().map(line -> mapTo(line, dueDate)).collect(Collectors.toList());
	}

	private PrescriptionLineDto mapTo(PrescriptionLineBo prescriptionLineBo, LocalDateTime dueDate) {
		var due = localDateMapper.fromLocalDateTime(LocalDateTime.now()).plusDays(30).isBefore(localDateMapper.fromLocalDateTime(dueDate));
		return PrescriptionLineDto.builder()
				.prescriptionLineNumber(prescriptionLineBo.getPrescriptionLineNumber())
				.prescriptionLineStatus(due ? PrescriptionValidStatesEnum.map(VENCIDO).toString() : prescriptionLineBo.getPrescriptionLineStatus())
				.dayDosis(prescriptionLineBo.getDayDosis())
				.presentation(prescriptionLineBo.getPresentation())
				.presentationQuantity(prescriptionLineBo.getPackageQuantity())
				.duration(prescriptionLineBo.getDuration())
				.unitDosis(prescriptionLineBo.getUnitDosis())
				.prescriptionProblemDto(mapTo(prescriptionLineBo.getPrescriptionProblemBo()))
				.genericMedicationDto(mapTo(prescriptionLineBo.getGenericMedicationBo()))
				.commercialMedicationDto(mapTo(prescriptionLineBo.getCommercialMedicationBo()))
				.quantity(prescriptionLineBo.getQuantity())
				.presentationPackageQuantity(prescriptionLineBo.getPresentationPackageQuantity())
				.suggestedCommercialMedicationDto(toSuggestedCommercialMedicationDto(prescriptionLineBo.getSuggestedCommercialMedicationBo()))
				.build();

	}

	private SuggestedCommercialMedicationDto toSuggestedCommercialMedicationDto(SuggestedCommercialMedicationBo suggestedCommercialMedicationBo) {
		return SuggestedCommercialMedicationDto.builder()
				.name(suggestedCommercialMedicationBo.getName())
				.snomedId(suggestedCommercialMedicationBo.getSnomedId())
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
				.professions(mapTo(professionalPrescriptionBo.getProfessions()))
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
				.name(patientPrescriptionBo.getName())
				.medicalCoverage(patientPrescriptionBo.getMedicalCoverage())
				.medicalCoverageCuit(patientPrescriptionBo.getMedicalCoverageCuit())
				.medicalCoveragePlan(patientPrescriptionBo.getMedicalCoveragePlan())
				.birthDate(patientPrescriptionBo.getBirthDate())
				.selfPerceivedName(patientPrescriptionBo.getSelfPerceivedName())
				.country(patientPrescriptionBo.getCountry())
				.province(patientPrescriptionBo.getProvince())
				.department(patientPrescriptionBo.getDepartment())
				.city(patientPrescriptionBo.getCity())
				.street(patientPrescriptionBo.getStreet())
				.streetNumber(patientPrescriptionBo.getStreetNumber())
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

	public ChangePrescriptionStateMultipleBo toChangePrescriptionStateMedicationBo(ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto, String identificationNumber) {
		return ChangePrescriptionStateMultipleBo.builder()
				.prescriptionId(changePrescriptionStateMultipleDto.getPrescriptionId())
				.pharmacyName(changePrescriptionStateMultipleDto.getPharmacyName())
				.pharmacistName(changePrescriptionStateMultipleDto.getPharmacistName())
				.pharmacistRegistration(changePrescriptionStateMultipleDto.getPharmacistRegistration())
				.changeDate(localDateMapper.fromStringToLocalDateTime(changePrescriptionStateMultipleDto.getChangeDate()))
				.changePrescriptionStateLineMedicationList(toChangePrescriptionStateMultipleMedicationBoList(changePrescriptionStateMultipleDto))
				.identificationNumber(identificationNumber)
				.build();
	}

	private List<ChangePrescriptionStateMultipleMedicationBo> toChangePrescriptionStateMultipleMedicationBoList(ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto) {
		return changePrescriptionStateMultipleDto.getChangePrescriptionStateLineMedicationList().stream()
				.map(line -> toChangePrescriptionStateMultipleMedicationBo(line, changePrescriptionStateMultipleDto))
				.collect(Collectors.toList());
	}

	private ChangePrescriptionStateMultipleMedicationBo toChangePrescriptionStateMultipleMedicationBo(ChangePrescriptionStateMultipleMedicationDto line,
																									  ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto) {
		return ChangePrescriptionStateMultipleMedicationBo.builder()
				.prescriptionLine(line.getPrescriptionLine())
				.prescriptionStateId(line.getPrescriptionStateId())
				.dispensedMedicationBos(toDispensedMedicationBoList(line, changePrescriptionStateMultipleDto))
				.observations(line.getObservations())
				.build();
	}

	private List<DispensedMedicationBo> toDispensedMedicationBoList(ChangePrescriptionStateMultipleMedicationDto line,
																	ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto) {
		return line.getDispensedMedicationDtos().stream()
				.map(medication -> toDispensedMedicationBo(medication, changePrescriptionStateMultipleDto))
				.collect(Collectors.toList());
	}

	private DispensedMedicationBo toDispensedMedicationBo(DispensedMedicationDto medication, ChangePrescriptionStateMultipleDto changePrescriptionStateMultipleDto) {
		return new DispensedMedicationBo(medication.getSnomedId(), medication.getCommercialName(), medication.getCommercialPresentation(), medication.getSoldUnits(), medication.getBrand(), medication.getPrice(), medication.getAffiliatePayment(), medication.getMedicalCoveragePayment(), changePrescriptionStateMultipleDto.getPharmacyName(), changePrescriptionStateMultipleDto.getPharmacistName(), medication.getObservations());
	}

	private PrescriptionSpecialtyDto mapTo(PrescriptionSpecialtyBo prescriptionSpecialtyBo) {
		if (prescriptionSpecialtyBo == null) {
			return new PrescriptionSpecialtyDto();
		}
		return PrescriptionSpecialtyDto.builder()
				.specialty(prescriptionSpecialtyBo.getSpecialty())
				.snomedId(prescriptionSpecialtyBo.getSnomedId())
				.build();
	}

	public PrescriptionV2Dto toPrescriptionV2Dto(PrescriptionV2Bo prescriptionV2Bo) {
		if (prescriptionV2Bo.getPrescriptionId() == null)
			throw new PrescriptionNotFoundException("La receta no existe");
		return PrescriptionV2Dto.builder()
				.domain(prescriptionV2Bo.getDomain())
				.prescriptionId(prescriptionV2Bo.getPrescriptionId())
				.dueDate(prescriptionV2Bo.getDueDate())
				.link(prescriptionV2Bo.getLink())
				.isArchived(prescriptionV2Bo.getIsArchived())
				.institutionPrescription(mapTo(prescriptionV2Bo.getInstitutionPrescription()))
				.prescriptionDate(prescriptionV2Bo.getPrescriptionDate())
				.patientPrescription(mapTo(prescriptionV2Bo.getPatientPrescription()))
				.professionalPrescription(mapTo(prescriptionV2Bo.getProfessionalPrescription()))
				.prescriptionLines(toPrescriptionLineV2DtoList(prescriptionV2Bo.getPrescriptionLines(), prescriptionV2Bo.getDueDate()))
				.prescriptionSpecialty(mapTo(prescriptionV2Bo.getPrescriptionSpecialty()))
				.build();
	}

	public PrescriptionV3Dto toPrescriptionV3Dto(PrescriptionV2Bo prescriptionV2Bo) {
		if (prescriptionV2Bo.getPrescriptionId() == null)
			throw new PrescriptionNotFoundException("La receta no existe");
		return PrescriptionV3Dto.builder()
				.domain(prescriptionV2Bo.getDomain())
				.prescriptionId(prescriptionV2Bo.getPrescriptionId())
				.dueDate(prescriptionV2Bo.getDueDate())
				.link(prescriptionV2Bo.getLink())
				.isArchived(prescriptionV2Bo.getIsArchived())
				.institutionPrescriptionDto(mapTo(prescriptionV2Bo.getInstitutionPrescription()))
				.prescriptionDate(prescriptionV2Bo.getPrescriptionDate())
				.patientPrescriptionDto(mapTo(prescriptionV2Bo.getPatientPrescription()))
				.professionalPrescriptionDto(mapTo(prescriptionV2Bo.getProfessionalPrescription()))
				.prescriptionsLineDto(toPrescriptionLineV3DtoList(prescriptionV2Bo.getPrescriptionLines(), prescriptionV2Bo.getDueDate()))
				.prescriptionSpecialtyDto(mapTo(prescriptionV2Bo.getPrescriptionSpecialty()))
				.build();
	}

	private List<PrescriptionLineV2Dto> toPrescriptionLineV2DtoList(List<PrescriptionLineV2Bo> prescriptionLines, LocalDateTime dueDate) {
		if (prescriptionLines == null)
			return new ArrayList<>();
		return prescriptionLines.stream().map(line -> toPrescriptionLineV2Dto(line, dueDate)).collect(Collectors.toList());
	}

	private List<PrescriptionLineV3Dto> toPrescriptionLineV3DtoList(List<PrescriptionLineV2Bo> prescriptionLines, LocalDateTime dueDate) {
		if (prescriptionLines == null)
			return new ArrayList<>();
		return prescriptionLines.stream().map(line -> toPrescriptionLineV3Dto(line, dueDate)).collect(Collectors.toList());
	}

	private PrescriptionDosageDto mapTo(PrescriptionDosageBo prescriptionDosageBo) {
		return PrescriptionDosageDto.builder()
				.unitDosis(prescriptionDosageBo.getUnitDosis())
				.dayDosis(prescriptionDosageBo.getDayDosis())
				.duration(prescriptionDosageBo.getDuration())
				.presentation(prescriptionDosageBo.getPresentation())
				.packageQuantity(prescriptionDosageBo.getPackageQuantity())
				.quantity(prescriptionDosageBo.getQuantity())
				.frequency(prescriptionDosageBo.getFrequency())
				.frequencyUnit(prescriptionDosageBo.getFrequencyUnit())
				.presentationPackageQuantity(prescriptionDosageBo.getPresentationPackageQuantity())
				.build();
	}

	private PrescriptionLineV2Dto toPrescriptionLineV2Dto(PrescriptionLineV2Bo line, LocalDateTime dueDate) {
		boolean due = localDateMapper.fromLocalDateTime(LocalDateTime.now()).plusDays(30).isBefore(localDateMapper.fromLocalDateTime(dueDate));
		return PrescriptionLineV2Dto.builder()
				.prescriptionLineNumber(line.getPrescriptionLineNumber())
				.prescriptionLineStatus(due ? PrescriptionValidStatesEnum.map(VENCIDO).toString() : line.getPrescriptionLineStatus())
				.prescriptionProblem(mapTo(line.getPrescriptionProblem()))
				.genericMedication(mapTo(line.getGenericMedication()))
				.commercialMedications(mapToCommercialMedicationDtoList(line.getCommercialMedications()))
				.prescriptionDosage(mapTo(line.getPrescriptionDosage()))
				.observation(line.getObservation())
				.suggestedCommercialMedication(toSuggestedCommercialMedicationDto(line.getSuggestedCommercialMedication()))
				.build();
	}

	private PrescriptionLineV3Dto toPrescriptionLineV3Dto(PrescriptionLineV2Bo line, LocalDateTime dueDate) {
		boolean due = localDateMapper.fromLocalDateTime(LocalDateTime.now()).plusDays(30).isBefore(localDateMapper.fromLocalDateTime(dueDate));
		return PrescriptionLineV3Dto.builder()
				.prescriptionLineNumber(line.getPrescriptionLineNumber())
				.prescriptionLineStatus(due ? PrescriptionValidStatesEnum.map(VENCIDO).toString() : line.getPrescriptionLineStatus())
				.prescriptionProblemDto(mapTo(line.getPrescriptionProblem()))
				.genericMedicationDto(mapTo(line.getGenericMedication()))
				.commercialMedicationsDto(mapToCommercialMedicationDtoList(line.getCommercialMedications()))
				.prescriptionDosageDto(mapTo(line.getPrescriptionDosage()))
				.observation(line.getObservation())
				.suggestedCommercialMedicationDto(toSuggestedCommercialMedicationDto(line.getSuggestedCommercialMedication()))
				.build();
	}
	private List<CommercialMedicationDto> mapToCommercialMedicationDtoList(List<CommercialMedicationBo> commercialMedications) {
		return commercialMedications.stream().map(commercialMedication -> CommercialMedicationDto.builder().name(commercialMedication.getName()).snomedId(commercialMedication.getSnomedId()).build()).collect(Collectors.toList());
	}

}
