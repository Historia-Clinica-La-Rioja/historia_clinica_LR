package ar.lamansys.sgh.publicapi.application;

import ar.lamansys.sgh.publicapi.application.fetchbedrelocationbyactivity.FetchBedRelocationByActivity;
import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FetchBedRelocationByActivityTest {

	private FetchBedRelocationByActivity fetchBedRelocationByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@BeforeEach
	void setup() {
		fetchBedRelocationByActivity = new FetchBedRelocationByActivity(activityInfoStorage);
	}

	@Test
	void bedRelocationSuccess() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityInfoStorage.getBedRelocationsByActivity(refsetCode, activityId)).thenReturn(
				Arrays.asList(
						new BedRelocationInfoBo(
								1,
								LocalDateTime.now(), "Cuidados Mínimos",
								new SnomedBo("1", "1")
						),
						new BedRelocationInfoBo(
								2,
								LocalDateTime.now(), "Intensiva",
								new SnomedBo("2", "2")
						)
				)
		);

		List<BedRelocationInfoBo> result = fetchBedRelocationByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 2);
	}

	@Test
	void bedRelocationFailed() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityInfoStorage.getBedRelocationsByActivity(refsetCode, activityId)).thenReturn(
				new ArrayList<>()
		);

		List<BedRelocationInfoBo> result = fetchBedRelocationByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 0);
	}
}