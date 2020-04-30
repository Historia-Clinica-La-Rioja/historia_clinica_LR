package net.pladema.internation.controller;

import net.pladema.BaseControllerTest;
import net.pladema.internation.controller.mapper.InternmentEpisodeMapper;
import net.pladema.internation.controller.mocks.MocksInternmentPatient;
import net.pladema.internation.service.InternmentEpisodeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InternmentEpisodeController.class)
public class InternmentEpisodeControllerTest extends BaseControllerTest {

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private InternmentEpisodeMapper internmentEpisodeMapper;

	@Before
	public void setup() {
	}

	@Test
	public void getInternmentSummary() throws Exception {
		final Integer internmentEpisodeId = 10;
		when(internmentEpisodeService.getIntermentSummary(internmentEpisodeId)).thenReturn(Optional.empty());
		when(internmentEpisodeMapper.toInternmentSummaryDto(any())).thenReturn(MocksInternmentPatient.mockInternmentSummary(internmentEpisodeId));
		final String URL = "/institutions/1/internments/" +internmentEpisodeId +"/summary";
		mockMvc.perform(get(URL))
				.andDo(log())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(internmentEpisodeId));
	}
	
}
