package ar.lamansys.sgh.publicapi.application;

import ar.lamansys.sgh.publicapi.application.fetchproceduresbyactivity.FetchProcedureByActivity;
import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchProcedureByActivityTest {

	private FetchProcedureByActivity fetchProcedureByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@BeforeEach
	void setup() {
		fetchProcedureByActivity = new FetchProcedureByActivity(activityInfoStorage);
	}

	@Test
	void procedureSuccess() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityInfoStorage.getProceduresByActivity(refsetCode, activityId)).thenReturn(
				Collections.singletonList(ProcedureInformationBo.builder()
						.snomedBo(new SnomedBo("1", "1"))
						.administrationTime(LocalDateTime.now())
						.build()));

		List<ProcedureInformationBo> result = fetchProcedureByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 1);
	}

	@Test
	void procedureFailed() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityInfoStorage.getProceduresByActivity(refsetCode, activityId)).thenReturn(
				Collections.emptyList()
		);

		List<ProcedureInformationBo> result = fetchProcedureByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 0);
	}
}
