package ar.lamansys.sgh.publicapi.imagecenter.application;

import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.UpdateSize;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.exceptions.UpdateSizeException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateMoveStudyAccessDeniedException;
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
public class UpdateSizeTest {

	@Mock
	private ImageCenterPublicApiPermissions imageCenterPublicApiPermissions;

	@Mock
	private SharedLoadStudiesResultPort moveStudiesService;

	@InjectMocks
	private UpdateSize updateSize;

	private Integer idMove;
	private Integer size;
	private String imageId;

	@BeforeEach
	public void setUp() {
		idMove = 1;
		size = 1024;
		imageId = "imageId";
	}

	@Test
	public void testRun_successful() throws UpdateSizeException {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);

		Boolean result = updateSize.run(idMove, size, imageId);

		assertTrue(result);
		verify(moveStudiesService, times(1)).updateSize(idMove, size, imageId);
	}

	@Test
	public void testRun_accessDenied() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(false);

		assertThrows(UpdateMoveStudyAccessDeniedException.class, () ->
				updateSize.run(idMove, size, imageId)
		);
	}

	@Test
	public void testRun_institutionNotFound() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.empty());

		assertThrows(UpdateMoveStudyAccessDeniedException.class, () ->
				updateSize.run(idMove, size, imageId)
		);
	}

	@Test
	public void testRun_exceptionThrown() {
		when(moveStudiesService.findInstitutionId(idMove)).thenReturn(Optional.of(1));
		when(imageCenterPublicApiPermissions.canUpdate(1)).thenReturn(true);
		doThrow(new RuntimeException("Test Exception")).when(moveStudiesService).updateSize(
				anyInt(),
				anyInt(),
				anyString()
		);

		assertThrows(UpdateSizeException.class, () ->
				updateSize.run(idMove, size, imageId)
		);
	}
}
