package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.activities.application.processactivity.ProcessActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessActivityControllerTest {

	private final String refsetCode = "REFSET_CODE";
	private final Long activityId = 1L;

	@Mock
	private ProcessActivity processActivity;

	@InjectMocks
	private ProcessActivityController processActivityController;

	@BeforeEach
	void setUp() {
		processActivityController = new ProcessActivityController(processActivity);
	}

	@Test
	void markActivityAsProcessed() {
		processActivityController.markActivityAsProcessed(refsetCode, activityId);
		verify(processActivity, times(1)).run(refsetCode, activityId);
	}

}