package net.pladema.internation.controller;


import net.pladema.BaseControllerTest;
import net.pladema.internation.controller.mapper.InternmentStateMapper;
import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.documents.anamnesis.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InternmentStateController.class)
public class InternmentStateControllerTest extends BaseControllerTest {

	@MockBean
	private InternmentStateService internmentStateService;

	@MockBean
	private HealthConditionService healthConditionService;

	@MockBean
	private MedicationService medicationService;

	@MockBean
	private AllergyService allergyService;

	@MockBean
	private InmunizationService inmunizationService;

	@MockBean
	private CreateVitalSignLabService createVitalSignLabService;

	@MockBean
	private InternmentStateMapper internmentStateMapper;

	@Before
	public void setup() {
	}

	@Test
	public void getGeneralState() throws Exception {
		final Integer internmentEpisodeId = 10;
		final String URL = "/institutions/1/internments-state/" +internmentEpisodeId +"/general";
		mockMvc.perform(get(URL))
				.andDo(log())
				.andExpect(status().isOk());
	}
	
}
