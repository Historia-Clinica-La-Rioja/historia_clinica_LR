package net.pladema.clinichistory.hospitalization.controller;


import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationAllergyState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationClinicalObservationState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationHealthConditionState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationImmunizationState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationMedicationState;
import net.pladema.IntegrationController;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentStateMapper;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.establishment.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternmentStateController.class)
class InternmentStateControllerIntegrationTest extends IntegrationController {

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


	@BeforeEach
	void setup() {
	}

	@Test
	@WithMockUser(authorities = {"ROOT"})
	void getGeneralState() throws Exception {
		final Integer internmentEpisodeId = 10;
		final String URL = "/institutions/1/internments-state/" +internmentEpisodeId +"/general";
		mockMvc.perform(get(URL))
				.andDo(log())
				.andExpect(status().isBadRequest());
	}
	
}
