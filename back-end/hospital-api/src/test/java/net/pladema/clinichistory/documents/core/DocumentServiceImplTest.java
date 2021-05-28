package net.pladema.clinichistory.documents.core;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
public class DocumentServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private DocumentServiceImpl documentServiceImpl;

	@MockBean
	private DocumentRepository documentRepository;

	@MockBean
	private DocumentHealthConditionRepository documentHealthConditionRepository;

	@MockBean
	private DocumentVitalSignRepository documentVitalSignRepository;

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

	@Before
	public void setUp() {
		documentServiceImpl = new DocumentServiceImpl(documentRepository, documentHealthConditionRepository,
                documentImmunizationRepository, documentProcedureRepository, documentVitalSignRepository, documentLabRepository,
				documentAllergyIntoleranceRepository, documentMedicamentionStatementRepository, documentDiagnosticReportRepository);
	}

	@Test
	public void test() {
		assertTrue(true);
	}
}
