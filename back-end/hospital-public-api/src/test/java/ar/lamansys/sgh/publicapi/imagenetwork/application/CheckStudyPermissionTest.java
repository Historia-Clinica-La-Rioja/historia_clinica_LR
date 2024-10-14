package ar.lamansys.sgh.publicapi.imagenetwork.application;

import ar.lamansys.sgh.publicapi.imagenetwork.application.check.CheckStudyPermission;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.CheckStudyAccessDeniedException;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.BadStudyTokenException;
import ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.service.ImageNetworkPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedStudyPermissionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckStudyPermissionTest {

	@Mock
	private SharedStudyPermissionPort sharedStudyPermissionPort;

	@Mock
	private ImageNetworkPublicApiPermissions imageNetworkPublicApiPermissions;

	@InjectMocks
	private CheckStudyPermission checkStudyPermission;

	private String studyInstanceUID;
	private String tokenStudy;

	@BeforeEach
	public void setUp() {
		studyInstanceUID = "studyInstanceUID";
		tokenStudy = "tokenStudy";
	}

	@Test
	public void testRun_successful() throws BadStudyTokenException {
		when(imageNetworkPublicApiPermissions.canAccess()).thenReturn(true);
		when(sharedStudyPermissionPort.checkTokenStudyPermissions(studyInstanceUID, tokenStudy)).thenReturn("PermissionGranted");

		String result = checkStudyPermission.run(studyInstanceUID, tokenStudy);

		assertEquals("PermissionGranted", result);
	}

	@Test
	public void testRun_accessDenied() {
		when(imageNetworkPublicApiPermissions.canAccess()).thenReturn(false);

		assertThrows(CheckStudyAccessDeniedException.class, () ->
				checkStudyPermission.run(studyInstanceUID, tokenStudy)
		);
	}

	@Test
	public void testRun_exceptionThrown() {
		when(imageNetworkPublicApiPermissions.canAccess()).thenReturn(true);
		when(sharedStudyPermissionPort.checkTokenStudyPermissions(studyInstanceUID, tokenStudy))
				.thenThrow(new RuntimeException("Test Exception"));

		assertThrows(BadStudyTokenException.class, () ->
				checkStudyPermission.run(studyInstanceUID, tokenStudy)
		);
	}
}
