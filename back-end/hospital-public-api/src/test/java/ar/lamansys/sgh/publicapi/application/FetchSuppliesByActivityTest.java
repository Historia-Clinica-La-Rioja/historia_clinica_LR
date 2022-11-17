package ar.lamansys.sgh.publicapi.application;
import ar.lamansys.sgh.publicapi.application.fetchsuppliesbyactivity.FetchSuppliesByActivity;
import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class FetchSuppliesByActivityTest {

	private FetchSuppliesByActivity fetchSuppliesByActivity;

	@Mock
	private ActivityInfoStorage activityInfoStorage;

	@BeforeEach
	void setup() {
		fetchSuppliesByActivity = new FetchSuppliesByActivity(activityInfoStorage);
	}

	@Test
	void procedureSuccess() {
		String refsetCode = "";
		Long activityId = 10L;

		when(activityInfoStorage.getSuppliesByActivity(refsetCode, activityId)).thenReturn(
				Collections.singletonList(SupplyInformationBo.builder()
						.supplyType("Vacuna")
						.status("Completo")
						.snomedBo(new SnomedBo("1", "1"))
						.build())
		);

		List<SupplyInformationBo> result = fetchSuppliesByActivity.run(refsetCode, activityId);
		Assertions.assertEquals(result.size(), 1);
	}
}