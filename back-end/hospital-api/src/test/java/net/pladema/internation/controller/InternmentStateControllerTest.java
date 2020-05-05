package net.pladema.internation.controller;


import net.pladema.BaseControllerTest;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.internation.controller.internment.InternmentStateController;
import net.pladema.internation.controller.internment.mapper.InternmentStateMapper;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.service.internment.InternmentStateService;
import net.pladema.internation.service.ips.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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
	private ClinicalObservationService clinicalObservationService;

	@MockBean
	private InternmentStateMapper internmentStateMapper;

	@MockBean
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@MockBean
	private InstitutionRepository institutionRepository;

	@Before
	public void setup() {
	}

	@Test
	@WithMockUser(authorities = {"ROOT"})
	public void getGeneralState() throws Exception {
		final Integer internmentEpisodeId = 10;
		final String URL = "/institutions/1/internments-state/" +internmentEpisodeId +"/general";
		mockMvc.perform(get(URL))
				.andDo(log())
				.andExpect(status().isBadRequest());
	}
	
}
