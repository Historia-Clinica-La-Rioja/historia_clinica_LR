package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.ObservationLabRepository;
import net.pladema.internation.repository.ips.ObservationVitalSignRepository;
import net.pladema.internation.service.documents.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CreateVitalSignLabServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private CreateVitalSignLabServiceImpl createVitalSignLabService;

	@MockBean
	private ObservationVitalSignRepository observationVitalSignRepository;

	@MockBean
	private ObservationLabRepository observationLabRepository;

	@MockBean
	private DocumentService documentService;

	@Before
	public void setUp() {
		createVitalSignLabService = new CreateVitalSignLabServiceImpl(observationVitalSignRepository,
				observationLabRepository, documentService);
	}


	@Test
	public void test() {
	}
}
