package ar.lamansys.sgh.publicapi.imagecenter.application;


import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.PossibleStudies;
import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.InsertResultStudiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.ResultStudiesException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.StudyDto;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service.ImageCenterPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedResultStudiesPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PossibleStudiesTest {

	@Mock
	private ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;

	@Mock
	private SharedResultStudiesPort resultStudiesService;

	@Mock
	private SharedLoadStudiesResultPort moveStudiesService;

	@InjectMocks
	private PossibleStudies possibleStudies;

	private Integer idMove;
	private Integer appointmentId;
	private List<StudyDto> studies;

	@BeforeEach
	public void setUp() {
		idMove = 1;
		appointmentId = 2;
		StudyDto study = StudyDto.builder()
				.patientId("patientId")
				.patientName("patientName")
				.studyDate("studyDate")
				.studyTime("studyTime")
				.modality("modality")
				.studyInstanceUid("studyInstanceUid")
				.build();
		studies = List.of(study);
	}

	@Test
	public void testRun_successful() throws ResultStudiesException {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);
		when(resultStudiesService.existsResult(idMove)).thenReturn(false);

		Boolean result = possibleStudies.run(idMove, appointmentId, studies);

		assertTrue(result);
		verify(resultStudiesService, times(1)).insertPossibleStudy(
				idMove,
				appointmentId,
				"patientId",
				"patientName",
				"studyDate",
				"studyTime",
				"modality",
				"studyInstanceUid"
		);
	}

	@Test
	public void testRun_withExistingResult() throws ResultStudiesException {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);
		when(resultStudiesService.existsResult(idMove)).thenReturn(true);

		Boolean result = possibleStudies.run(idMove, appointmentId, studies);

		assertTrue(result);
		verify(resultStudiesService, times(1)).deleteResult(idMove);
		verify(resultStudiesService, times(1)).insertPossibleStudy(
				idMove,
				appointmentId,
				"patientId",
				"patientName",
				"studyDate",
				"studyTime",
				"modality",
				"studyInstanceUid"
		);
	}

	@Test
	public void testRun_accessDenied() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(false);

		assertThrows(InsertResultStudiesAccessDeniedException.class, () ->
				possibleStudies.run(idMove, appointmentId, studies)
		);
	}

	@Test
	public void testRun_institutionNotFound() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.empty());

		assertThrows(InsertResultStudiesAccessDeniedException.class, () ->
				possibleStudies.run(idMove, appointmentId, studies)
		);
	}

	@Test
	public void testRun_exceptionThrown() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);
		when(resultStudiesService.existsResult(idMove)).thenReturn(false);
		doThrow(new RuntimeException("Test Exception")).when(resultStudiesService).insertPossibleStudy(
				anyInt(),
				anyInt(),
				anyString(),
				anyString(),
				anyString(),
				anyString(),
				anyString(),
				anyString()
		);

		assertThrows(ResultStudiesException.class, () ->
				possibleStudies.run(idMove, appointmentId, studies)
		);
	}
}