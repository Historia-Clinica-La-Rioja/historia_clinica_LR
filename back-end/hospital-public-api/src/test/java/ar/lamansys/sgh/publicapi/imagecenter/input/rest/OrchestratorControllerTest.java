package ar.lamansys.sgh.publicapi.imagecenter.input.rest;

import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.PossibleStudies;
import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.ResultStudiesException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.UpdateResult;

import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateResultException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.UpdateSize;

import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.exceptions.UpdateSizeException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.OrchestratorController;

import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.MoveResultDto;

import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.PossibleStudiesDto;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.SizeResultDto;

import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.StudyDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrchestratorControllerTest {

	@Mock
	private UpdateResult updateResult;

	@Mock
	private UpdateSize updateSize;

	@Mock
	private PossibleStudies possibleStudies;

	private OrchestratorController controller;

	@BeforeEach
	public void setUp() {
		controller = new OrchestratorController(updateResult, updateSize, possibleStudies);
	}

	@Test
	public void testUpdateResult() throws UpdateResultException {
		MoveResultDto moveResultDto = MoveResultDto.builder()
				.idMove(1)
				.result("result")
				.status("status")
				.build();

		moveResultDto.setResult("newResult");
		assertEquals("Expected 'result' to be 'newResult'", "newResult", moveResultDto.getResult());
		assertEquals("Expected toString to match", "MoveResultDto(idMove=1, result=newResult, status=status)", moveResultDto.toString());

		when(updateResult.run(moveResultDto.getIdMove(), moveResultDto.getStatus(), moveResultDto.getResult())).thenReturn(true);
		Boolean result = controller.updateResult(moveResultDto);
		assertEquals("Expected the updateResult to return true", true, result);
	}

	@Test
	public void testSetSizeStudy() throws UpdateSizeException {
		SizeResultDto sizeResultDto = SizeResultDto.builder()
				.idMove(1)
				.size(1)
				.imageId("imageId")
				.build();

		sizeResultDto.setSize(2);
		assertEquals("Expected 'size' to be '2'", 2, sizeResultDto.getSize());
		assertEquals("Expected toString to match", "SizeResultDto(idMove=1, size=2, imageId=imageId)", sizeResultDto.toString());

		when(updateSize.run(sizeResultDto.getIdMove(), sizeResultDto.getSize(), sizeResultDto.getImageId())).thenReturn(true);
		Boolean result = controller.setSizeStudy(sizeResultDto);
		assertEquals("Expected the setSizeStudy to return true", true, result);
	}

	@Test
	public void testSetPossibleStudies() throws ResultStudiesException {
		StudyDto study = StudyDto.builder()
				.patientId("patientId")
				.patientName("patientName")
				.studyDate("studyDate")
				.studyTime("studyTime")
				.modality("modality")
				.studyInstanceUid("studyInstanceUid")
				.build();

		List<StudyDto> studies = new java.util.ArrayList<>(List.of(study));

		PossibleStudiesDto possibleStudiesDto = PossibleStudiesDto.builder()
				.idMove(1)
				.appointmentId(2)
				.studies(studies)
				.build();

		study.setPatientName("newPatientName");
		assertEquals("Expected 'patientName' to be 'newPatientName'", "newPatientName", study.getPatientName());
		assertEquals("Expected toString to match", "StudyDto(patientId=patientId, patientName=newPatientName, studyDate=studyDate, studyTime=studyTime, modality=modality, studyInstanceUid=studyInstanceUid)", study.toString());

		possibleStudiesDto.setAppointmentId(3);
		assertEquals("Expected 'appointmentId' to be '3'", 3, possibleStudiesDto.getAppointmentId());
		assertEquals("Expected toString to match", "PossibleStudiesDto(appointmentId=3, idMove=1, studies=[StudyDto(patientId=patientId, patientName=newPatientName, studyDate=studyDate, studyTime=studyTime, modality=modality, studyInstanceUid=studyInstanceUid)])", possibleStudiesDto.toString());

		when(possibleStudies.run(possibleStudiesDto.getIdMove(), possibleStudiesDto.getAppointmentId(), possibleStudiesDto.getStudies())).thenReturn(true);
		Boolean result = controller.setPossibleStudies(possibleStudiesDto);
		assertEquals("Expected the setPossibleStudies to return true", true, result);
	}
}
