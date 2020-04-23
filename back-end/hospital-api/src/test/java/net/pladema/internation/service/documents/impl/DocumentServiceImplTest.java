package net.pladema.internation.service.documents.impl;

import net.pladema.internation.repository.core.DocumentHealthConditionRepository;
import net.pladema.internation.repository.core.DocumentLabRepository;
import net.pladema.internation.repository.core.DocumentVitalSignRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DocumentServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private DocumentServiceImpl documentServiceImpl;

	@MockBean
	private DocumentHealthConditionRepository documentHealthConditionRepository;

	@MockBean
	private DocumentVitalSignRepository documentVitalSignRepository;

	@MockBean
	private DocumentLabRepository documentLabRepository;

	@Before
	public void setUp() {
		documentServiceImpl = new DocumentServiceImpl(documentHealthConditionRepository,
				documentVitalSignRepository, documentLabRepository);
	}

	@Test
	public void test() {
	}
}
