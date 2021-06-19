package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.AnamnesisServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
public class AnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private AnamnesisServiceImpl anamnesisServiceImpl;

	@MockBean
	private DocumentService documentService;

	@MockBean
	private NoteService noteService;

	@Before
	public void setUp() {
		anamnesisServiceImpl = new AnamnesisServiceImpl(documentService, noteService);
	}

	@Test
	public void test() {
		assertTrue(true);
	}
}
