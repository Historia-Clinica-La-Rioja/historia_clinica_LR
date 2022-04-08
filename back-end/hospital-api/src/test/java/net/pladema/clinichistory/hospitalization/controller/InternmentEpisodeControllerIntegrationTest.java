package net.pladema.clinichistory.hospitalization.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.events.SimplePublishService;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.IntegrationController;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleContactMapper;
import net.pladema.clinichistory.hospitalization.controller.mocks.MocksInternmentPatient;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.service.patientdischarge.PatientDischargeService;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternmentEpisodeController.class)
class InternmentEpisodeControllerIntegrationTest extends IntegrationController {

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

	@MockBean
	private ResponsibleContactMapper responsibleContactMapper;

	@MockBean
	private LocalDateMapper localDateMapper;

	@MockBean
	private SimplePublishService simplePublishService;

	@MockBean
	private HospitalApiPublisher hospitalApiPublisher;

	@BeforeEach
	void setup() {
	}

	private void configContextInternmentValid(){
		when(internmentEpisodeRepository.existsById(anyInt())).thenReturn(true);
		when(institutionRepository.existsById(anyInt())).thenReturn(true);
	}
	
	@Test
	@WithMockUser(authorities = {"ROOT"})
	void getInternmentSummary() throws Exception {
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
