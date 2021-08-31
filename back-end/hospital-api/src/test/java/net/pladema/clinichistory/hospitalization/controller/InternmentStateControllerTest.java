package net.pladema.clinichistory.hospitalization.controller;


import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.*;
import net.pladema.UnitController;
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
	private FetchHospitalizationGeneralState fetchHospitalizationGeneralState;

	@MockBean
	private FetchHospitalizationHealthConditionState fetchHospitalizationHealthConditionState;

	@MockBean
	private FetchHospitalizationMedicationState fetchHospitalizationMedicationState;

	@MockBean
	private FetchHospitalizationAllergyState fetchHospitalizationAllergyState;

	@MockBean
	private FetchHospitalizationImmunizationState fetchHospitalizationImmunizationState;

	@MockBean
	private FetchHospitalizationClinicalObservationState fetchHospitalizationClinicalObservationState;

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
