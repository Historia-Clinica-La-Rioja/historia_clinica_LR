package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import java.util.Optional;
import net.pladema.IntegrationController;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.DeleteAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.establishment.repository.InstitutionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnamnesisController.class)
class AnamnesisControllerIntegrationTest extends IntegrationController {

	private static final Long DOCUMENT_ID = 1L;
	private static final String POST = "/institutions/1/internments/1/anamnesis";
	private static final String PUT = "/institutions/1/internments/1/anamnesis/" + DOCUMENT_ID;
	private static final String GET = "/institutions/1/internments/1/anamnesis/" + DOCUMENT_ID;

	@MockBean
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@MockBean
	private InstitutionRepository institutionRepository;

	@MockBean
	private DocumentRepository documentRepository;

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private CreateAnamnesisService createAnamnesisService;

	@MockBean
	private AnamnesisService anamnesisService;

	@MockBean
	private AnamnesisMapper anamnesisMapper;

	@MockBean
	private DeleteAnamnesisService deleteAnamnesisService;

	@MockBean
	private UpdateAnamnesisService updateAnamnesisService;

	@MockBean
	private SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

	@BeforeEach
	void setup() {
		buildMockMvc();
	}

	@Test
	@WithUserDetails(value="user-24-ESPECIALISTA_MEDICO", userDetailsServiceBeanName="UserDetailsServiceWithRole")
	void test_createAnamnesisFailed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithUserDetails(value="user-24-ESPECIALISTA_MEDICO", userDetailsServiceBeanName="UserDetailsServiceWithRole")
	void test_getAnamnesisSuccess() throws Exception {
		configContextDocumentValid();
		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isOk());
	}

	private void configContextInternmentValid(){
		when(internmentEpisodeRepository.existsById(anyInt())).thenReturn(true);
		when(institutionRepository.existsById(anyInt())).thenReturn(true);
	}

	private void configContextDocumentValid(){
		configContextInternmentValid();
		when(documentRepository.findById(DOCUMENT_ID)).thenReturn(mockDocument());
	}

	private static Optional<Document> mockDocument() {
		Document mock = new Document();
		mock.setId(DOCUMENT_ID);
		mock.setTypeId(DocumentType.ANAMNESIS);
		mock.setStatusId(DocumentStatus.FINAL);
		return Optional.of(mock);
	}


}
