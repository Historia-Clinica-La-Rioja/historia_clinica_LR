package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.application.fetchactivitybyid.FetchActivityById;
import ar.lamansys.sgh.publicapi.application.fetchbedrelocationbyactivity.FetchBedRelocationByActivity;
import ar.lamansys.sgh.publicapi.application.fetchdocumentsinfobyactivity.FetchDocumentsInfoByActivity;

import ar.lamansys.sgh.publicapi.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.application.fetchsuppliesbyactivity.FetchSuppliesByActivity;
import ar.lamansys.sgh.publicapi.application.processactivity.ProcessActivity;
import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DocumentInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.ActivitiesMapper;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityInfoControllerTest {

	private ActivityInfoController activityInfoController;
	@Mock
	private ActivitiesMapper activitiesMapper;
	@Mock
	private FetchActivityById fetchActivityById;
	@Mock
	private FetchProcedureByActivity fetchProcedureByActivity;
	@Mock
	private ProcessActivity processActivity;
	@Mock
	private FetchSuppliesByActivity fetchSuppliesByActivity;
	@Mock
	private FetchBedRelocationByActivity fetchBedRelocationByActivity;
	@Mock
	private FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	@BeforeEach
	void setUp() {
		activityInfoController = new ActivityInfoController(activitiesMapper,
				fetchActivityById,
				fetchProcedureByActivity,
				processActivity,
				fetchSuppliesByActivity,
				fetchBedRelocationByActivity,
				fetchDocumentsInfoByActivity);
	}


	@Test
	void getDocumentsInfoByActivity() {
		var document = DocumentInfoBo.builder()
				.id(1L)
				.updateOn(LocalDateTime.of(2020,1,1,1,1))
				.filePath("FILE_PATH")
				.fileName("FILE_NAME")
				.type("TYPE")
				.build();
		when(fetchDocumentsInfoByActivity.run(any(), any())).thenReturn(List.of(document));
		var documentDto = DocumentInfoDto.builder()
				.id(1L)
				.updateOn(new DateTimeDto(new DateDto(2020,1,1), new TimeDto(1,1,1)))
				.filePath("FILE_PATH")
				.fileName("FILE_NAME")
				.type("TYPE")
				.build();
		when(fetchDocumentsInfoByActivity.run(any(), any())).thenReturn(List.of(document));
		when(activitiesMapper.mapToListDocumentInfoDto(any())).thenReturn(List.of(documentDto));

		var result = activityInfoController.
				getDocumentsInfoByActivity("REFSET_CODE", 1L);

		verify(fetchDocumentsInfoByActivity, times(1)).run("REFSET_CODE", 1L);
		verify(activitiesMapper, times(1)).mapToListDocumentInfoDto(List.of(document));

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(List.of(documentDto), result);
	}
}