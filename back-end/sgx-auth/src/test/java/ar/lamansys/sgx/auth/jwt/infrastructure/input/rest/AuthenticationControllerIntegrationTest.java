package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.function.Consumer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.ResultActions;

import ar.lamansys.sgx.auth.AuthAutoConfiguration;
import ar.lamansys.sgx.auth.IntegrationTest;
import ar.lamansys.sgx.auth.jwt.application.login.Login;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginEnumException;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.RefreshToken;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.recaptcha.service.ICaptchaService;
import ar.lamansys.sgx.shared.recaptcha.service.impl.RecaptchaInvalid;

@AutoConfigureMockMvc
@SpringBootTest(classes = {AuthAutoConfiguration.class})
@TestPropertySource("classpath:integration-test.properties")
@Import({DateTimeProvider.class})
@Disabled
class AuthenticationControllerIntegrationTest extends IntegrationTest {

	@Value("/auth")
	protected String authURL;

	@MockBean
	private Login login;

	@MockBean
	private RefreshToken refreshToken;

	@MockBean
	private ICaptchaService iCaptchaService;
	
	@Test
	@DisplayName("Login should return token")
	void loginShouldReturnTokens() throws Exception {
		performLogin(
				serviceResult -> serviceResult.thenReturn(new JWTokenBo("T", "RF"))
		).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("T"))
				.andExpect(jsonPath("$.refreshToken").value("RF"));
	}

	@Test
	@DisplayName("Login should fail invalid credentials")
	void loginShouldFailIfInvalidCredentials() throws Exception {
		performLogin(
				serviceResult -> serviceResult.thenThrow(new BadLoginException(BadLoginEnumException.BAD_CREDENTIALS, "invalid-credentials"))
		).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("invalid-credentials"))
				.andExpect(jsonPath("$.text").value("Credenciales inválidas"));
	}

	@Test
	@DisplayName("Login should fail recaptcha invalid")
	void loginShouldFailIfRecaptchaInvalid() throws Exception {
		when(iCaptchaService.isRecaptchaEnable()).thenReturn(true);
		doThrow(new RecaptchaInvalid()).when(iCaptchaService).validRecaptcha(anyString(), anyString());

		performLogin(
				serviceResult -> serviceResult.thenReturn(new JWTokenBo("T", "RF"))
		)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("invalid-recaptcha"))
			.andExpect(jsonPath("$.text").value("Recatpcha inválido"));
	}

	@Test
	@DisplayName("Refresh token success")
	void refreshShouldRefreshValidToken() throws Exception {
		performRefresh(
				serviceResult -> serviceResult.thenReturn(new JWTokenBo("Nuevo token", "Nuevo refresh"))
		)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("Nuevo token"))
				.andExpect(jsonPath("$.refreshToken").value("Nuevo refresh"));
	}

	@Test
	@DisplayName("Refresh should fail invalid refresh token")
	void refreshShouldFailIfInvalidRefreshToken() throws Exception {
		performRefresh(
				serviceResult -> serviceResult.thenThrow(new BadRefreshTokenException())
		)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value("invalid-token"))
				.andExpect(jsonPath("$.text").value("Refresh token inválido"));
	}

	private ResultActions performLogin(Consumer<OngoingStubbing<JWTokenBo>> serviceResult) throws Exception {
		serviceResult.accept(when(login.execute(any())));
		String body = "{\"username\":\"pepe@pepe.com\",\"password\":\"password\"}";
		return mockMvc.perform(post(authURL).content(body).contentType(MediaType.APPLICATION_JSON)
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", "http://www.someurl.com")
				.header("recaptcha", "somestring"));
	}

	private ResultActions performRefresh(Consumer<OngoingStubbing<JWTokenBo>> serviceResult) throws Exception {
		serviceResult.accept(when(refreshToken.execute("un token")));
		String body = "{\"refreshToken\":\"un token\"}";
		return mockMvc.perform(
				post(authURL + "/refresh")
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
		);
	}

}
