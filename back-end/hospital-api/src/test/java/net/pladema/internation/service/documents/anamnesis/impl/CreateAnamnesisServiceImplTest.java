package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AllergyService;
import net.pladema.internation.service.documents.anamnesis.CreateVitalSignLabService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CreateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

	@MockBean
	private DocumentService documentService;

	@MockBean
	private NoteService noteService;

	@MockBean
	private HealthConditionService healthConditionService;

	@MockBean
	private AllergyService allergyService;

	@MockBean
	private CreateVitalSignLabService createVitalSignLabService;

	@Before
	public void setUp() {
		createAnamnesisServiceImpl = new CreateAnamnesisServiceImpl(
				documentService,
				noteService,
				healthConditionService,
				allergyService,
				createVitalSignLabService);
	}


	@Test
	public void test() {
	}
}
