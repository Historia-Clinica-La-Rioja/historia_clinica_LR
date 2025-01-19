package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.ActivitySearchFilter;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.FetchActivitiesByFilter;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.mapper.ActivitiesMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActivitiesControllerTest {

	@Mock
	private ActivitiesMapper activitiesMapper;

	@Mock
	private FetchActivitiesByFilter fetchActivitiesByFilter;

	@Mock
	private LocalDateMapper localDateMapper;

	@InjectMocks
	private ActivitiesController activitiesController;

	private final String refsetCode = "123";
	private final String from = "2024-01-01";
	private final String to = "2024-12-31";
	private final Boolean reprocessing = false;

	@BeforeEach
	public void setUp() {
		when(localDateMapper.fromStringToLocalDate(from)).thenReturn(LocalDate.of(2024, 1, 1));
		when(localDateMapper.fromStringToLocalDate(to)).thenReturn(LocalDate.of(2024, 12, 31));
	}

	@Test
	void getActivitiesByInstitutionTest() {
		List<AttentionInfoDto> attentionInfoDtoList = Collections.singletonList(
				AttentionInfoDto.builder().id(1L).build()
		);

		when(fetchActivitiesByFilter.run(any(ActivitySearchFilter.class))).thenReturn(Collections.emptyList());
		when(activitiesMapper.mapTo(Collections.emptyList())).thenReturn(Collections.emptyList());
		when(activitiesMapper.groupDiagnosis(Collections.emptyList())).thenReturn(attentionInfoDtoList);

		ResponseEntity<List<AttentionInfoDto>> response = activitiesController.getActivitiesByInstitution(refsetCode, from, to, reprocessing);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(1, response.getBody().size());
		assertEquals(1L, response.getBody().get(0).getId());
	}

	@Test
	void getActivitiesByInstitutionAndPatientTest() {
		String identificationNumber = "111";
		List<AttentionInfoDto> attentionInfoDtoList = Collections.singletonList(
				AttentionInfoDto.builder().id(1L).build()
		);

		when(fetchActivitiesByFilter.run(any(ActivitySearchFilter.class))).thenReturn(Collections.emptyList());
		when(activitiesMapper.mapTo(Collections.emptyList())).thenReturn(Collections.emptyList());
		when(activitiesMapper.groupDiagnosis(Collections.emptyList())).thenReturn(attentionInfoDtoList);

		ResponseEntity<List<AttentionInfoDto>> response = activitiesController.getActivitiesByInstitutionAndPatient(refsetCode, identificationNumber, from, to, reprocessing);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(1, response.getBody().size());
		assertEquals(1L, response.getBody().get(0).getId());
	}

	@Test
	void getActivitiesByInstitutionAndCoverageTest() {
		String coverageCuit = "123";
		List<AttentionInfoDto> attentionInfoDtoList = Collections.singletonList(
				AttentionInfoDto.builder().id(1L).build()
		);

		when(fetchActivitiesByFilter.run(any(ActivitySearchFilter.class))).thenReturn(Collections.emptyList());
		when(activitiesMapper.mapTo(Collections.emptyList())).thenReturn(Collections.emptyList());
		when(activitiesMapper.groupDiagnosis(Collections.emptyList())).thenReturn(attentionInfoDtoList);

		ResponseEntity<List<AttentionInfoDto>> response = activitiesController.getActivitiesByInstitutionAndCoverage(refsetCode, coverageCuit, from, to, reprocessing);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(1, response.getBody().size());
		assertEquals(1L, response.getBody().get(0).getId());
	}
}