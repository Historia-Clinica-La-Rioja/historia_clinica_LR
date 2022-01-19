package net.pladema.clinichistory.hospitalization.controller;

import net.pladema.IntegrationController;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternmentPatientController.class)
class InternmentPatientControllerIntegrationTest extends IntegrationController {

	@MockBean
	private InternmentPatientService internmentPatientService;

	@MockBean
	private InternmentEpisodeMapper internmentEpisodeMapper;

	@MockBean
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@BeforeEach
	void setup() {
	}

	@Test
	@WithMockUser(authorities = {"ROOT"})
	void getAllInternmentPatient() throws Exception {
		final Integer institutionId = 10;
		final String URL = "/institutions/ "+institutionId+"/internments/patients";
		mockMvc.perform(get(URL))
			.andDo(log())
			.andExpect(status().isOk());
			//.andExpect(jsonPath("$", hasSize(institutionId)));
	}
	
}
