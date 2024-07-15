package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.FetchActivityById;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivityNotFoundException;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.GenderEnum;
import ar.lamansys.sgh.publicapi.activities.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoExtendedBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.activities.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedCIE10Bo;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.ClinicalSpecialityDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.CoverageActivityInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.DiagnosesDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.InternmentDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.PersonExtendedInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.PersonInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.ProfessionalDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SnomedCIE10Dto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SnomedDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.mapper.ActivitiesMapper;
import ar.lamansys.sgh.publicapi.domain.ScopeEnum;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.domain.datetimeutils.TimeBo;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class ActivityInfoByInstitutionControllerTest {

	@Mock
	private ActivitiesMapper activitiesMapper;

	@Mock
	private FetchActivityById fetchActivityById;

	@InjectMocks
	private ActivityInfoByInstitutionController activityInfoByInstitutionController;

	private AttentionInfoDto attentionInfoDto;

	private AttentionInfoBo attentionInfoBo;

	@BeforeEach
	void setUp() {
		attentionInfoBo = new AttentionInfoBo();
		attentionInfoBo.setId(1L);
		attentionInfoBo.setEncounterId(123L);
		attentionInfoBo.setAttentionDate(LocalDate.of(2023, 5, 10));
		attentionInfoBo.setSpeciality(new SnomedBo("1234", "Speciality"));
		attentionInfoBo.setPatient(new PersonInfoBo("12345678", "John", "Doe", LocalDate.of(1990, 1, 1), GenderEnum.FEMALE));
		attentionInfoBo.setCoverage(new CoverageActivityInfoBo("12345", true, "20123456789", "Basic Plan"));
		attentionInfoBo.setScope(ScopeEnum.AMBULATORIA);
		attentionInfoBo.setInternmentInfo(new InternmentBo("INT123", LocalDateTime.of(2023, 5, 1, 0, 0), LocalDateTime.of(2023, 5, 5, 0, 0)));
		attentionInfoBo.setResponsibleDoctor(new ProfessionalBo(1, "Dr. Smith", "Smith", "MED123", "987654321"));

		DateBo attentionDateBo = new DateBo(2024, 5, 10);
		TimeBo attentionTimeBo = new TimeBo(0, 0, 0);
		DateTimeBo attentionDateTimeBo = new DateTimeBo(attentionDateBo, attentionTimeBo);
		attentionInfoBo.setAttentionDateWithTime(attentionDateTimeBo);

		SnomedCIE10Bo mainDiagnosisBo = new SnomedCIE10Bo("5678", "Main Diagnosis", "A00");
		SingleDiagnosticBo singleDiagnosticBo = new SingleDiagnosticBo(
				mainDiagnosisBo,
				true,
				"diagnosisType",
				"diagnosisVerificationStatus",
				LocalDateTime.now()
		);
		attentionInfoBo.setSingleDiagnosticBo(singleDiagnosticBo);

		DateBo emergencyCareDateBo = new DateBo(2024, 5, 10);
		TimeBo emergencyCareTimeBo = new TimeBo(0, 0, 0);
		DateTimeBo emergencyCareDateTimeBo = new DateTimeBo(emergencyCareDateBo, emergencyCareTimeBo);
		attentionInfoBo.setEmergencyCareAdministrativeDischargeDateTime(emergencyCareDateTimeBo);

		DateDto attentionDate = new DateDto(2024, 5, 10);
		DateTimeDto attentionDateWithTime = new DateTimeDto(attentionDate, null);

		ClinicalSpecialityDto speciality = ClinicalSpecialityDto.builder()
				.snomed(SnomedDto.builder()
						.sctId("1234")
						.pt("Speciality")
						.build())
				.build();

		PersonInfoDto patient = PersonInfoDto.builder()
				.identificationNumber("12345678")
				.firstName("John")
				.lastName("Doe")
				.birthDate(new DateDto(2000, 1, 1))
				.genderId((short) 1)
				.build();

		CoverageActivityInfoDto coverage = CoverageActivityInfoDto.builder()
				.affiliateNumber("12345")
				.attentionCoverage(true)
				.cuitCoverage("20123456789")
				.plan("Basic Plan")
				.build();

		InternmentDto internmentInfo = InternmentDto.builder()
				.id("INT123")
				.entryDate(new DateTimeDto(new DateDto(2024, 5, 1), null))
				.dischargeDate(new DateTimeDto(new DateDto(2024, 5, 5), null))
				.build();

		ProfessionalDto responsibleDoctor = ProfessionalDto.builder()
				.id(1)
				.firstName("Dr. Smith")
				.lastName("Smith")
				.licenceNumber("MED123")
				.indentificationNumber("987654321")
				.build();

		SnomedCIE10Dto mainDiagnosis = SnomedCIE10Dto.builder()
				.sctId("5678")
				.pt("Main Diagnosis")
				.CIE10Id("A00")
				.build();

		DiagnosesDto diagnoses = new DiagnosesDto(mainDiagnosis, Collections.singletonList(mainDiagnosis));

		PersonExtendedInfoDto personExtendedInfo = PersonExtendedInfoDto.builder()
				.middleNames("Middle")
				.otherLastNames("OtherLast")
				.email("john.doe@example.com")
				.nameSelfDetermination("John")
				.genderSelfDeterminationId((short) 2)
				.build();

		attentionInfoDto = AttentionInfoDto.builder()
				.id(1L)
				.encounterId(123L)
				.attentionDate(attentionDate)
				.speciality(speciality)
				.patient(patient)
				.coverage(coverage)
				.scope("Scope")
				.internmentInfo(internmentInfo)
				.responsibleDoctor(responsibleDoctor)
				.diagnoses(diagnoses)
				.attentionDateWithTime(attentionDateWithTime)
				.personExtendedInfo(personExtendedInfo)
				.emergencyCareAdministrativeDischargeDateTime(new DateTimeDto(new DateDto(2024, 5, 10), null))
				.build();
	}

	@Test
	void testGetActivityByInstitutionSuccess() throws ActivityNotFoundException {
		when(fetchActivityById.run(anyString(), anyLong())).thenReturn(attentionInfoBo);
		when(activitiesMapper.mapToAttentionInfoDto(attentionInfoBo)).thenReturn(attentionInfoDto);

		AttentionInfoDto result = activityInfoByInstitutionController.getActivityByInstitution("testRefset", 1L);
		assertEquals(attentionInfoDto, result);
	}

	@Test
	void testGetActivityByInstitutionNotFound() throws ActivityNotFoundException {
		when(fetchActivityById.run(anyString(), anyLong())).thenThrow(new ActivityNotFoundException("refsetCode", 1L));

		assertThrows(ActivityNotFoundException.class, () -> {
			activityInfoByInstitutionController.getActivityByInstitution("refsetCode", 1L);
		});
	}
}