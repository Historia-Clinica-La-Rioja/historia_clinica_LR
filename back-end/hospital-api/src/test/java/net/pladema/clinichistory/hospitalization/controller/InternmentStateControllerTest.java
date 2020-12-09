package net.pladema.clinichistory.hospitalization.controller;


import net.pladema.UnitController;
import net.pladema.clinichistory.documents.service.generalstate.*;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentStateMapper;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.establishment.repository.InstitutionRepository;
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
public class InternmentStateControllerTest extends UnitController {

	@MockBean
	private EncounterGeneralStateBuilder encounterGeneralStateBuilder;

	@MockBean
	private HealthConditionGeneralStateService healthConditionGeneralStateService;

	@MockBean
	private MedicationGeneralStateService medicationGeneralStateService;

	@MockBean
	private AllergyGeneralStateService allergyGeneralStateServiceService;

	@MockBean
	private ImmunizationGeneralStateService immunizationGeneralStateService;

	@MockBean
	private ClinicalObservationGeneralStateService clinicalObservationGeneralStateService;

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
