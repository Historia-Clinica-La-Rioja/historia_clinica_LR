package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.CreateAnamnesisServiceImpl;
import net.pladema.clinichistory.documents.service.ips.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class CreateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

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
	private ClinicalObservationService clinicalObservationService;

	@MockBean
	private ImmunizationService immunizationService;

	@MockBean
	private MedicationService medicationService;

	@MockBean
	private ProceduresService proceduresService;

	@Before
	public void setUp() {
		createAnamnesisServiceImpl = new CreateAnamnesisServiceImpl(
				documentService,
				internmentEpisodeService,
				noteService,
				healthConditionService,
				allergyService,
                clinicalObservationService,
                immunizationService,
				proceduresService,
				medicationService);
	}

	@Test
	public void test() {
		assertTrue(true);
	}

}
