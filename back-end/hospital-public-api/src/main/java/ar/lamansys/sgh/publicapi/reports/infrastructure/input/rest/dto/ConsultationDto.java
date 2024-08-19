package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import java.util.List;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConsultationDto {
	private Integer appointmentId;
	private Integer consultationId;
	private DateTimeDto consultationDate;
	private String institutionCode;
	private String institutionName;
	private HierarchicalUnitDto serviceHierarchicalUnit;
	private HierarchicalUnitDto hierarchicalUnit;
	private SnomedClinicalSpecialtyDto clinicalSpecialty;
	private IdentificationDto identification;
	private MedicalCoverageNameDto medicalCoverage;
	private String officialGender;
	private DateDto birthdate;
	private Integer age;
	private String department;
	private String city;
	private String appointmentState;
	private String appointmentBookingChannel;
	private List<ConsultationItemWithDateDto> reasons;
	private List<ConsultationItemWithDateDto> procedures;
	private List<ConsultationItemWithDateDto> problems;

}
