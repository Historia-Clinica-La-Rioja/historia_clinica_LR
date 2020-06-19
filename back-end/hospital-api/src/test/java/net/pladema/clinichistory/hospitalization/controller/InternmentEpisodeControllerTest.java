package net.pladema.clinichistory.hospitalization.controller;

import net.pladema.BaseControllerTest;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.controller.mocks.MocksInternmentPatient;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.clinichistory.documents.repository.DocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.service.patientDischarge.PatientDischargeService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

	@MockBean
	private PatientDischargeRepository patientDischargeRepository;

	@MockBean
	private PatientDischargeService patientDischargeService;
	
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
