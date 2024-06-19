package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.activities.application.fetchbedrelocationbyactivity.FetchBedRelocationByActivity;
import ar.lamansys.sgh.publicapi.activities.application.fetchdocumentsinfobyactivity.FetchDocumentsInfoByActivity;

import ar.lamansys.sgh.publicapi.activities.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.activities.application.fetchsuppliesbyactivity.FetchSuppliesByActivity;
import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.BedRelocationInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ClinicalSpecialityDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DocumentInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ProcedureInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SupplyInformationDto;
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
	private String refsetCode = "REFSET_CODE";
	private Long activityId = 1L;

	@Mock
	private ActivitiesMapper activitiesMapper;
	@Mock
	private FetchProcedureByActivity fetchProcedureByActivity;
	@Mock
	private FetchSuppliesByActivity fetchSuppliesByActivity;
	@Mock
	private FetchBedRelocationByActivity fetchBedRelocationByActivity;
	@Mock
	private FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	@BeforeEach
	void setUp() {
		activityInfoController = new ActivityInfoController(activitiesMapper,
				fetchProcedureByActivity,
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

	@Test
	void getProceduresByActivity() {
		var snomedBo = SnomedBo.builder().sctId("12345").pt("procedure").build();
		var snomedDto = activitiesMapper.mapTo(snomedBo);
		var procedure = ProcedureInformationBo.builder()
				.snomedBo(snomedBo)
				.administrationTime(LocalDateTime.of(2020, 1, 1, 1, 1))
				.build();
		when(fetchProcedureByActivity.run(any(), any())).thenReturn(List.of(procedure));
		var procedureDto = ProcedureInformationDto.builder()
				.snomed(snomedDto)
				.date(new DateTimeDto(new DateDto(2020, 1, 1), new TimeDto(1, 1, 1)))
				.build();
		when(activitiesMapper.mapTo(procedure)).thenReturn(procedureDto);

		var result = activityInfoController.getProceduresByActivity("REFSET_CODE", 1L);

		verify(fetchProcedureByActivity, times(1)).run("REFSET_CODE", 1L);
		verify(activitiesMapper, times(1)).mapTo(procedure);

		assertFalse(result.getBody().isEmpty());
		assertEquals(1, result.getBody().size());
		assertEquals(procedureDto, result.getBody().get(0));
	}

	@Test
	void getSuppliesByActivity() {
		var snomedBo = SnomedBo.builder().sctId("12345").pt("supply").build();
		var snomedDto = activitiesMapper.mapTo(snomedBo);
		var supply = SupplyInformationBo.builder()
				.supplyType("type")
				.status("status")
				.snomedBo(snomedBo)
				.administrationTime(LocalDateTime.of(2020, 1, 1, 1, 1))
				.build();
		when(fetchSuppliesByActivity.run(any(), any())).thenReturn(List.of(supply));
		var supplyDto = SupplyInformationDto.builder()
				.supplyType("type")
				.status("status")
				.snomed(snomedDto)
				.administrationTime(LocalDateTime.of(2020, 1, 1, 1, 1))
				.build();
		when(activitiesMapper.mapTo(supply)).thenReturn(supplyDto);

		var result = activityInfoController.getSuppliesByActivity("REFSET_CODE", 1L);

		verify(fetchSuppliesByActivity, times(1)).run("REFSET_CODE", 1L);
		verify(activitiesMapper, times(1)).mapTo(supply);

		assertFalse(result.getBody().isEmpty());
		assertEquals(1, result.getBody().size());
		assertEquals(supplyDto, result.getBody().get(0));
	}

	@Test
	void getBedRelocationsByActivity() {
		var snomedBo = SnomedBo.builder().sctId("12345").pt("bedrelocation").build();
		var snomedDto = activitiesMapper.mapTo(snomedBo);
		var specialityDto = ClinicalSpecialityDto.builder().snomed(snomedDto).build();
		var bedRelocation = BedRelocationInfoBo.builder()
				.bedId(1)
				.relocationDate(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
				.careType("care")
				.service(snomedBo)
				.build();
		when(fetchBedRelocationByActivity.run(any(), any())).thenReturn(List.of(bedRelocation));
		var bedRelocationDto = BedRelocationInfoDto.builder()
				.bedId(1)
				.relocationDate(new DateTimeDto(new DateDto(2020, 1, 1), new TimeDto(1, 1, 1)))
				.careType("care")
				.service(specialityDto)
				.build();
		when(activitiesMapper.mapToBedRelocation(any())).thenReturn(List.of(bedRelocationDto));

		var result = activityInfoController.getBedRelocationsByActivity(refsetCode, activityId);

		verify(fetchBedRelocationByActivity, times(1)).run(refsetCode, activityId);
		verify(activitiesMapper, times(1)).mapToBedRelocation(any());

		assertFalse(result.getBody().isEmpty());
		assertEquals(1, result.getBody().size());
		assertEquals(bedRelocationDto, result.getBody().get(0));
	}

}