package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.mapper;

import java.util.List;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchconsultationsbydate.ConsultationItemWithDateBo;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.ConsultationDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.ConsultationItemWithDateDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.HierarchicalUnitDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.IdentificationDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.MedicalCoverageDto;
import ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto.SnomedClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

public class FetchConsultationsByDateMapper {

	public static ConsultationDto fromBo(ConsultationBo consultationBo) {

		var serviceHierarchicalUnit = consultationBo.getServiceHierarchicalUnit();
		var hierarchicalUnit = consultationBo.getHierarchicalUnit();
		var birthdate = consultationBo.getBirthdate();

		return ConsultationDto.builder()
				.appointmentId(consultationBo.getAppointmentId())
				.consultationId(consultationBo.getConsultationId())
				.consultationDate(new DateTimeDto(
						new DateDto(
								consultationBo.getConsultationDate().getDate().getYear(),
								consultationBo.getConsultationDate().getDate().getMonth(),
								consultationBo.getConsultationDate().getDate().getDay()
						),
						new TimeDto(
								consultationBo.getConsultationDate().getTime().getHours(),
								consultationBo.getConsultationDate().getTime().getMinutes(),
								consultationBo.getConsultationDate().getTime().getSeconds()
						)
				))
				.institutionCode(consultationBo.getInstitutionCode())
				.institutionName(consultationBo.getInstitutionName())
				.serviceHierarchicalUnit(HierarchicalUnitDto.builder()
						.id(serviceHierarchicalUnit != null ? serviceHierarchicalUnit.getId() : null)
						.description(serviceHierarchicalUnit != null ? serviceHierarchicalUnit.getDescription() : null)
						.type(serviceHierarchicalUnit != null ? serviceHierarchicalUnit.getType() : null)
						.build())
				.hierarchicalUnit(HierarchicalUnitDto.builder()
						.id(hierarchicalUnit != null ? hierarchicalUnit.getId() : null)
						.description(hierarchicalUnit != null ? hierarchicalUnit.getDescription() : null)
						.type(hierarchicalUnit != null ? hierarchicalUnit.getType() : null)
						.build())
				.clinicalSpecialty(SnomedClinicalSpecialtyDto.builder()
						.description(consultationBo.getClinicalSpecialty().getDescription())
						.snomedId(consultationBo.getClinicalSpecialty().getSnomedId())
						.id(consultationBo.getClinicalSpecialty().getId())
						.build()
				)
				.identification(IdentificationDto.builder()
						.identificationType(consultationBo.getIdentification().getIdentificationType())
						.identificationNumber(consultationBo.getIdentification().getIdentificationNumber())
						.build())
				.medicalCoverage(MedicalCoverageDto.builder()
						.name(consultationBo.getMedicalCoverage().getName())
						.rnos(consultationBo.getMedicalCoverage().getRnos())
						.build())
				.officialGender(consultationBo.getOfficialGender())
				.birthdate(birthdate != null ? new DateDto(
						birthdate.getYear(),
						birthdate.getMonth(),
						birthdate.getDay()
				) : null)
				.age(consultationBo.getAge())
				.department(consultationBo.getDepartment())
				.city(consultationBo.getCity())
				.appointmentState(consultationBo.getAppointmentState())
				.appointmentBookingChannel(consultationBo.getAppointmentBookingChannel())
				.problems(mapToList(consultationBo.getProblems()))
				.reasons(mapToList(consultationBo.getReasons()))
				.procedures(mapToList(consultationBo.getProcedures()))
				.build();
	}

	private static List<ConsultationItemWithDateDto> mapToList(List<ConsultationItemWithDateBo> l) {
		return l.stream()
				.map( e -> ConsultationItemWithDateDto.builder()
						.sctId(e.getSctId())
						.pt(e.getPt())
						.cie10Id(e.getCie10Id())
						.startDate(e.getStartDate())
						.endDate(e.getEndDate())
						.build())
				.collect(Collectors.toList());
	}
}
