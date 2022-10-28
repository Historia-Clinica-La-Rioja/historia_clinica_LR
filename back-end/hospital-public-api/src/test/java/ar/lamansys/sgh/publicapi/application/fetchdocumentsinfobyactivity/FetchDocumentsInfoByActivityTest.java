package ar.lamansys.sgh.publicapi.application.fetchdocumentsinfobyactivity;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;

import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchDocumentsInfoByActivityTest {

	private FetchDocumentsInfoByActivity fetchDocumentsInfoByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@BeforeEach
	void setUp() {
		fetchDocumentsInfoByActivity = new FetchDocumentsInfoByActivity(activityInfoStorage);
	}

	@Test
	void run() {
		var document = DocumentInfoBo.builder()
				.id(1L)
				.updateOn(LocalDateTime.of(2020,1,1,1,1))
				.filePath("FILE_PATH")
				.fileName("FILE_NAME")
				.type("TYPE")
				.build();
		when(activityInfoStorage.getDocumentsByActivity(any(), any())).thenReturn(List.of(document));
		var result = fetchDocumentsInfoByActivity.run("refset", 1L);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(List.of(document), result);
	}

}