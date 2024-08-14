package ar.lamansys.sgh.publicapi.imagecenter.application;

import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.UpdateResult;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateMoveStudyAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateResultException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service.ImageCenterPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateResultTest {

	@Mock
	private ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;

	@Mock
	private SharedLoadStudiesResultPort moveStudiesService;

	@InjectMocks
	private UpdateResult updateResult;

	private Integer idMove;
	private String status;
	private String result;

	@BeforeEach
	public void setUp() {
		idMove = 1;
		status = "Completed";
		result = "Normal";
	}

	@Test
	public void testRun_successful() throws UpdateResultException {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);

		Boolean res = updateResult.run(idMove, status, result);

		assertTrue(res);
		verify(moveStudiesService, times(1)).updateStatusAndResult(idMove, status, result);
	}

	@Test
	public void testRun_accessDenied() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(false);

		assertThrows(UpdateMoveStudyAccessDeniedException.class, () ->
				updateResult.run(idMove, status, result)
		);
	}

	@Test
	public void testRun_institutionNotFound() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.empty());

		assertThrows(UpdateMoveStudyAccessDeniedException.class, () ->
				updateResult.run(idMove, status, result)
		);
	}

	@Test
	public void testRun_exceptionThrown() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);
		doThrow(new RuntimeException("Test Exception")).when(moveStudiesService).updateStatusAndResult(
				anyInt(),
				anyString(),
				anyString()
		);

		assertThrows(UpdateResultException.class, () ->
				updateResult.run(idMove, status, result)
		);
	}
}
