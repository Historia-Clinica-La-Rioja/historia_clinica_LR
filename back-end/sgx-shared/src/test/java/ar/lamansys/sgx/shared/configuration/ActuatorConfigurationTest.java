package ar.lamansys.sgx.shared.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(MockitoExtension.class)
class ActuatorConfigurationTest {

	@Test
	void test_not_authenticated() {
		ActuatorConfiguration test = new ActuatorConfiguration();
		test.setWhiteList(Arrays.asList("10.0.0.0/16","127.0.0.1/32"));
		String result = test.getAccessInfo();

		assertThat(result)
				.isNotNull()
				.isEqualTo("permitAll() and ( hasIpAddress('10.0.0.0/16') or hasIpAddress('127.0.0.1/32') )");
	}

	@Test
	void test_not_authenticated_empty() {
		ActuatorConfiguration test = new ActuatorConfiguration();
		String result = test.getAccessInfo();

		assertThat(result)
				.isNotNull()
				.isEqualTo("permitAll()");
	}


	@Test
	void test_is_authenticated() {
		ActuatorConfiguration test = new ActuatorConfiguration();
		test.setWhiteList(Arrays.asList("10.0.0.0/16","127.0.0.1/32"));
		test.setAuthenticated(true);
		String result = test.getAccessInfo();

		assertThat(result)
				.isNotNull()
				.isEqualTo("isAuthenticated() and ( hasIpAddress('10.0.0.0/16') or hasIpAddress('127.0.0.1/32') )");
	}

	@Test
	void test_is_authenticated_empty() {
		ActuatorConfiguration test = new ActuatorConfiguration();
		test.setAuthenticated(true);
		String result = test.getAccessInfo();

		assertThat(result)
				.isNotNull()
				.isEqualTo("isAuthenticated()");
	}
}
