package net.pladema.clinichistory.documents.service.impl;

import net.pladema.clinichistory.documents.repository.*;
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
	private DocumentInmunizationRepository documentInmunizationRepository;

	@MockBean
	private DocumentMedicamentionStatementRepository documentMedicamentionStatementRepository;

	@Before
	public void setUp() {
		documentServiceImpl = new DocumentServiceImpl(documentRepository, documentHealthConditionRepository,
				documentInmunizationRepository, documentVitalSignRepository, documentLabRepository,
				documentAllergyIntoleranceRepository, documentMedicamentionStatementRepository);
	}

	@Test
	public void test() {
		assertTrue(true);
	}
}
