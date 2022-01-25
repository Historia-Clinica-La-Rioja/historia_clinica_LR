package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.AnamnesisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class AnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private AnamnesisServiceImpl anamnesisServiceImpl;

	@Mock
	private DocumentService documentService;

	@Mock
	private NoteService noteService;

	@BeforeEach
	void setUp() {
		anamnesisServiceImpl = new AnamnesisServiceImpl(documentService, noteService);
	}

	@Test
	void test() {
		assertTrue(true);
	}
}
