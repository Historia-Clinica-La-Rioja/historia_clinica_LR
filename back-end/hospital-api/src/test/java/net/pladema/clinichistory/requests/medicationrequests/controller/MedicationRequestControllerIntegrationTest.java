package net.pladema.clinichistory.requests.medicationrequests.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.pdf.PdfService;
import net.pladema.IntegrationController;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.CreateMedicationRequestMapper;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.ListMedicationInfoMapper;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicationRequestController.class)
class MedicationRequestControllerIntegrationTest extends IntegrationController {

    private String BASE_PATH = "/institutions/{institutionId}/patient/{patientId}/medication-requests";

    @MockBean
    private CreateMedicationRequestService createMedicationRequestService;

    @MockBean
    private HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    @MockBean
    private CreateMedicationRequestMapper createMedicationRequestMapper;

    @MockBean
    private ListMedicationInfoMapper medicationRequestMapper;

    @MockBean
    private ListMedicationInfoService listMedicationInfoService;

    @MockBean
    private ChangeStateMedicationService changeStateMedicationService;

    @MockBean
    private PatientExternalService patientExternalService;

    @MockBean
    private GetMedicationRequestInfoService getMedicationRequestInfoService;

    @MockBean
    private PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

    @MockBean
    private PdfService pdfService;

	@MockBean
	private FeatureFlagsService featureFlagsService;

	@MockBean
	private DocumentAuthorFinder documentAuthorFinder;

    @BeforeEach
    void setup() {
        this.buildMockMvc();
    }

    @Test
    @WithUserDetails(value="user-24-ESPECIALISTA_MEDICO", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    void test_createWithoutSnomed_fail() throws Exception {
        String URL = BASE_PATH
                    .replace("{institutionId}","1")
                    .replace("{patientId}", "1");
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockWithoutSnomed()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*")
                        .value(buildMessage("value.mandatory")));
    }


    private String mockWithoutSnomed() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"dosage\": {\n" +
                "        \"chronic\": true,\n" +
                "        \"diary\": true,\n" +
                "        \"duration\": 0,\n" +
                "        \"frequency\": 0\n" +
                "      },\n" +
                "      \"healthConditionId\": 1,\n" +
                "      \"observations\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }

    @Test
    @WithUserDetails(value="user-24-ESPECIALISTA_MEDICO", userDetailsServiceBeanName="UserDetailsServiceWithRole")
    void test_createWithoutItems_fail() throws Exception {
        String URL = BASE_PATH
                .replace("{institutionId}","1")
                .replace("{patientId}", "1");
        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockWithoutItems()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.items")
                        .value("must not be empty"));
    }

    private String mockWithoutItems() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }

    private String mockWithoutHealthConditionId() {
        return "{\n" +
                "  \"hasRecipe\": true,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"dosage\": {\n" +
                "        \"chronic\": true,\n" +
                "        \"diary\": true,\n" +
                "        \"duration\": 0,\n" +
                "        \"frequency\": 0\n" +
                "      },\n" +
                "      \"observations\": \"string\",\n" +
                "      \"snomed\": {\n" +
                "        \"sctid\": \"string\",\n" +
                "        \"parentFsn\": \"string\",\n" +
                "        \"parentId\": \"string\",\n" +
                "        \"pt\": \"string\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"medicalCoverageId\": 0\n" +
                "}";
    }


}