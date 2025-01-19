package ar.lamansys.sgh.clinichistory.infrastructure.application.document;

import ar.lamansys.sgh.clinichistory.application.document.DocumentServiceImpl;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentAllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentDiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentExternalCauseRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentHealthcareProfessionalRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentMedicamentionStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentObstetricEventRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentOdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProsthesisRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentReportSnomedConceptRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentTriageRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private DocumentServiceImpl documentServiceImpl;

	@MockBean
	private DocumentRepository documentRepository;

	@MockBean
	private DocumentHealthConditionRepository documentHealthConditionRepository;

	@MockBean
	private DocumentRiskFactorRepository documentRiskFactorRepository;

	@MockBean
	private DocumentLabRepository documentLabRepository;

	@MockBean
	private DocumentAllergyIntoleranceRepository documentAllergyIntoleranceRepository;

	@MockBean
	private DocumentImmunizationRepository documentImmunizationRepository;

	@MockBean
	private DocumentProcedureRepository documentProcedureRepository;

	@MockBean
	private DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

	@MockBean
	private DocumentDiagnosticReportRepository documentDiagnosticReportRepository;

	@MockBean
	private DocumentOdontologyProcedureRepository documentOdontologyProcedureRepository;

	@MockBean
	private DocumentOdontologyDiagnosticRepository documentOdontologyDiagnosticRepository;

	@MockBean
	private DocumentExternalCauseRepository documentExternalCauseRepository;

	@MockBean
	private DocumentObstetricEventRepository documentObstetricEventRepository;
	
	@MockBean
	private DocumentTriageRepository documentTriageRepository;

	@MockBean
	private DocumentReportSnomedConceptRepository documentReportSnomedConceptRepository;

	@MockBean
	private SnomedService snomedService;

	@MockBean
	private DocumentHealthcareProfessionalRepository documentHealthcareProfessionalRepository;

	@MockBean
	private DocumentProsthesisRepository documentProsthesisRepository;

	@InjectMocks
	private DocumentServiceImpl documentService;

	@BeforeEach
	void setUp() {
	}

	@Test
	void test() {
		assertTrue(true);
	}
}
