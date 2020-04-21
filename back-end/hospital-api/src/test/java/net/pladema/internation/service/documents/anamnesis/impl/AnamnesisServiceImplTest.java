package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.security.service.impl.SecurityServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class AnamnesisServiceImplTest {

	private static final String TOKEN = "TOKEN";

	private AnamnesisServiceImpl anamnesisServiceImpl;

	@Before
	public void setUp() {
		anamnesisServiceImpl = new AnamnesisServiceImpl();
	}


	@Test
	public void test() {
	}
}
