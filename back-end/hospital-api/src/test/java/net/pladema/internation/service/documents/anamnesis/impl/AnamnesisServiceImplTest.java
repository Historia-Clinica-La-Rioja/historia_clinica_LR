package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.service.general.NoteService;
import net.pladema.internation.service.documents.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
	}
}
