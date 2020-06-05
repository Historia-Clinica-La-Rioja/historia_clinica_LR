package net.pladema.internation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.internation.repository.documents.DocumentRepository;
import net.pladema.internation.service.internment.ResponsibleContactService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseControllerTest;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.internation.controller.internment.InternmentEpisodeController;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.controller.internment.mapper.PatientDischargeMapper;
import net.pladema.internation.controller.mocks.MocksInternmentPatient;
import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RunWith(SpringRunner.class)
@WebMvcTest(InternmentEpisodeController.class)
public class InternmentEpisodeControllerTest extends BaseControllerTest {

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private InternmentEpisodeMapper internmentEpisodeMapper;

	@MockBean
	private HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	
	@MockBean
	private PatientDischargeMapper patientDischargeMapper;

	@MockBean
	private BedExternalService bedExternalService;

	@MockBean
	private ResponsibleContactService responsibleContactService;

	@MockBean
	private InternmentEpisodeRepository internmentEpisodeRepository;
	
	@MockBean
	private InstitutionRepository institutionRepository;

	@MockBean
	private FeatureFlagsService featureFlagsService;

	@MockBean
	private DocumentRepository documentRepository;
	
	@Before
	public void setup() {
	}

	private void configContextInternmentValid(){
		when(internmentEpisodeRepository.existsById(anyInt())).thenReturn(true);
		when(institutionRepository.existsById(anyInt())).thenReturn(true);
	}
	
	@Test
	@WithMockUser(authorities = {"ROOT"})
	public void getInternmentSummary() throws Exception {
		final Integer internmentEpisodeId = 10;
		when(internmentEpisodeService.getIntermentSummary(internmentEpisodeId)).thenReturn(Optional.empty());
		when(internmentEpisodeMapper.toInternmentSummaryDto(any())).thenReturn(MocksInternmentPatient.mockInternmentSummary(internmentEpisodeId));
		configContextInternmentValid();
		
		final String URL = "/institutions/1/internments/" +internmentEpisodeId +"/summary";
		mockMvc.perform(get(URL))
				.andDo(log())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(internmentEpisodeId));
	}
	
}
