package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.HealthConditionRepository;
import net.pladema.internation.repository.masterdata.NoteRepository;
import net.pladema.internation.repository.masterdata.SnomedRepository;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.HealthConditionService;
import net.pladema.security.service.impl.SecurityServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class CreateAnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

	@MockBean
	private HealthConditionService healthConditionService;

	@Before
	public void setUp() {
		createAnamnesisServiceImpl = new CreateAnamnesisServiceImpl(healthConditionService);
	}


	@Test
	public void test() {
	}
}
