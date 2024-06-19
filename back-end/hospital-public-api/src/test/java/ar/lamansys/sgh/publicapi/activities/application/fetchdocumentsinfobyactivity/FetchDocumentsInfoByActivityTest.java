package ar.lamansys.sgh.publicapi.activities.application.fetchdocumentsinfobyactivity;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivitiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchDocumentsInfoByActivityTest {

	private FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	void setUp() {
		fetchDocumentsInfoByActivity = new FetchDocumentsInfoByActivity(activityInfoStorage,activitiesPublicApiPermissions);
	}

	@Test
	void run() {
		allowAccessPermission(true);
		var document = DocumentInfoBo.builder()
				.id(1L)
				.updateOn(LocalDateTime.of(2020,1,1,1,1))
				.filePath("FILE_PATH")
				.fileName("FILE_NAME")
				.type("TYPE")
				.build();
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		when(activityInfoStorage.getDocumentsByActivity(any(), any())).thenReturn(List.of(document));
		var result = fetchDocumentsInfoByActivity.run("", 1L);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(List.of(document), result);
	}

	@Test
	void failProcessActivityAccessDeniedException(){
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(ActivitiesAccessDeniedException.class,
				() -> fetchDocumentsInfoByActivity.run("",10L));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canAccessActivityInfo(1)).thenReturn(canAccess);
	}

}