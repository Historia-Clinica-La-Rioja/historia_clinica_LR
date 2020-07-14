package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis;

import net.pladema.BaseControllerTest;
import net.pladema.clinichistory.documents.repository.DocumentRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleDoctorMapper;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.clinichistory.hospitalization.controller.generalstate.constraint.validator.EffectiveVitalSignTimeValidator;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.DiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.HealthConditionDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.featureflags.controller.constraints.validators.SGHNotNullValidator;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.pdf.service.PdfService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnamnesisController.class)
public class AnamnesisControllerTest extends BaseControllerTest {

	private static final Long DOCUMENTID = 1L;
	private static final String POST = "/institutions/1/internments/1/anamnesis";
	private static final String PUT = "/institutions/1/internments/1/anamnesis/" + DOCUMENTID;
	private static final String GET = "/institutions/1/internments/1/anamnesis/" + DOCUMENTID;

	private static final String OTITIS = "43275000";

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private CreateAnamnesisService createAnamnesisService;

	@MockBean
	private UpdateAnamnesisService updateAnamnesisService;

	@MockBean
	private AnamnesisService anamnesisService;

	@MockBean
	private AnamnesisMapper anamnesisMapper;

	@MockBean
	private PatientExternalService patientExternalService;

	@MockBean
	private PdfService pdfService;

	@MockBean
	private ResponsibleDoctorMapper responsibleDoctorMapper;

	@MockBean
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@MockBean
	private InstitutionRepository institutionRepository;

	@MockBean
	private DocumentRepository documentRepository;

	@MockBean
	private EffectiveVitalSignTimeValidator effectiveVitalSignTimeValidator;

	@MockBean
	private SGHNotNullValidator sghNotNullValidator;

	@MockBean
	private FeatureFlagsService featureFlagsService;

	@Autowired
	private MessageSource messageSource;

	@Before
	public void setup() {
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisSuccess() throws Exception {
		configContextAnamnesisValid();
		configContextPatientExist();
		when(effectiveVitalSignTimeValidator.isValid(any(), any())).thenReturn(true);
		when(sghNotNullValidator.isValid(any(), any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockRequestBodyBasic()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisFailed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisWithoutMainDiagnosis() throws Exception {
		configContextAnamnesisValid();
		when(featureFlagsService.isOn(any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockEmptyAnamnesisDto()))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.mainDiagnosis").value(buildMessage("diagnosis.mandatory")));
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisWithMainDiagnosisInvalid() throws Exception {
		configContextAnamnesisValid();
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockAnamnesisDtoWithDiagnosis()))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors")
						.value(buildMessageWithPath("diagnosis.main.repeated", "createAnamnesis.anamnesisDto.diagnosis")));
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisWithoutPatient() throws Exception {
		configContextAnamnesisValid();
		when(sghNotNullValidator.isValid(any(), any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post(POST)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockRequestBodyBasic()))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
	}

	@Test
	@WithMockUser
	public void test_createAnamnesisWithVitalSignInvalid(){
		//to complete
	}

	@Test
	@WithMockUser
	public void test_getAnamnesisSuccess() throws Exception {
		configContextDocumentValid();
		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void test_getAnamnesisFailed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void test_getAnamnesisDocumentInvalid() throws Exception {
		configContextInternmentValid();
		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors")
						.value(buildMessageWithPath("document.invalid", "getAnamnesis.anamnesisId")));
	}

	@Test
	@WithMockUser
	public void test_updateAnamnesisSuccess() throws Exception {
		configContextDocumentValid();
		configContextPatientExist();
		when(effectiveVitalSignTimeValidator.isValid(any(), any())).thenReturn(true);
		when(sghNotNullValidator.isValid(any(), any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.put(PUT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockRequestBodyBasic()))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void test_updateAnamnesisFailed() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.put(PUT))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void test_updateAnamnesisDocumentInvalid() throws Exception {
		configContextInternmentValid();
		when(sghNotNullValidator.isValid(any(), any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.put(PUT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockRequestBodyBasic()))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors")
						.value(buildMessageWithPath("document.invalid", "updateAnamnesis.anamnesisId")));
	}

	@Test
	@WithMockUser
	public void test_updateAnamnesisWithoutPatient() throws Exception {
		configContextAnamnesisValid();
		configContextDocumentValid();
		when(effectiveVitalSignTimeValidator.isValid(any(), any())).thenReturn(true);
		when(sghNotNullValidator.isValid(any(), any())).thenReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.put(PUT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mockRequestBodyBasic()))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
	}

	@Test
	@WithMockUser
	public void test_updateAnamnesisWithVitalSignInvalid(){
		//to complete
	}

	private void configContextInternmentValid(){
		when(internmentEpisodeRepository.existsById(anyInt())).thenReturn(true);
		when(institutionRepository.existsById(anyInt())).thenReturn(true);
	}

	private void configContextAnamnesisValid(){
		configContextInternmentValid();
		when(internmentEpisodeService.haveAnamnesis(any())).thenReturn(false);
	}

	private void configContextDocumentValid(){
		configContextInternmentValid();
		when(documentRepository.findById(DOCUMENTID)).thenReturn(mockDocument());
	}

	private void configContextPatientExist(){
		when(internmentEpisodeService.getPatient(any())).thenReturn(Optional.of(1));
	}

	private static Optional<Document> mockDocument(){
		Document mock = new Document();
		mock.setId(DOCUMENTID);
		mock.setTypeId(DocumentType.ANAMNESIS);
		mock.setStatusId(DocumentStatus.DRAFT);
		return Optional.of(mock);
	}

	private String mockRequestBodyBasic() throws Exception {
		return objectMapper.writeValueAsString(mockBasicAnamnesisDto());
	}

	private String mockEmptyAnamnesisDto() throws Exception {
		return objectMapper.writeValueAsString(new AnamnesisDto());
	}

	private static AnamnesisDto mockBasicAnamnesisDto(){
		AnamnesisDto mock = new AnamnesisDto();
		mock.setMainDiagnosis(mockHealthConditionDto());
		return mock;
	}

	private String mockAnamnesisDtoWithDiagnosis() throws Exception {
		AnamnesisDto mock = mockBasicAnamnesisDto();
		mock.setDiagnosis(Collections.singletonList(mockDiagnosisDto()));
		return objectMapper.writeValueAsString(mock);
	}

	private static HealthConditionDto mockHealthConditionDto(){
		HealthConditionDto diagnosis = new HealthConditionDto();
		diagnosis.setSnomed(mockSnomed());
		return diagnosis;
	}

	private static DiagnosisDto mockDiagnosisDto(){
		DiagnosisDto diagnosis = new DiagnosisDto();
		diagnosis.setSnomed(mockSnomed());
		return diagnosis;
	}

	private static SnomedDto mockSnomed(){
		SnomedDto mock = new SnomedDto();
		mock.setId(OTITIS);
		mock.setPt("Preferred Term");
		return mock;
	}

	private String buildMessageWithPath(String keyMessage, String path){
		return path + ": " + buildMessage(keyMessage);
	}

	private String buildMessage(String keyMessage){
		return messageSource.getMessage(keyMessage, null, Locale.getDefault());
	}
}
