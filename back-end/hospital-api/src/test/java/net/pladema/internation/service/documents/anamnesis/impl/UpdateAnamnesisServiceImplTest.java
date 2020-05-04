package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.InternmentEpisodeService;
import net.pladema.internation.service.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UpdateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private UpdateAnamnesisServiceImpl updateAnamnesisServiceImpl;

	@MockBean
	private DocumentService documentService;

	@MockBean
	private InternmentEpisodeService internmentEpisodeService;

	@MockBean
	private NoteService noteService;

	@MockBean
	private HealthConditionService healthConditionService;

	@MockBean
	private AllergyService allergyService;

	@MockBean
	private MedicationService medicationService;

	@MockBean
	private VitalSignLabService vitalSignLabService;

	@MockBean
	private InmunizationService inmunizationService;

	@Before
	public void setUp() {
		updateAnamnesisServiceImpl = new UpdateAnamnesisServiceImpl(documentService, internmentEpisodeService,
				noteService, healthConditionService, allergyService, medicationService, vitalSignLabService,
				inmunizationService);
	}


	@Test
	public void test() {
	}
}
