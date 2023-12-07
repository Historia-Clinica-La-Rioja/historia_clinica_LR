package ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import ar.lamansys.sgx.auth.UnitRepository;
import ar.lamansys.sgx.auth.apiKey.application.delete.DeleteUserApiKey;
import ar.lamansys.sgx.auth.apiKey.application.generate.GenerateUserApiKey;
import ar.lamansys.sgx.auth.apiKey.application.list.ListUserApiKeys;
import ar.lamansys.sgx.auth.apiKey.application.login.ApiKeyLogin;
import ar.lamansys.sgx.auth.apiKey.application.login.ApiKeyLoginImpl;
import ar.lamansys.sgx.auth.apiKey.domain.exceptions.DuplicateKeyNameException;
import ar.lamansys.sgx.auth.apiKey.domain.exceptions.InvalidKeyNameException;
import ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto.GenerateApiKeyDto;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.ApiKeyStorageImpl;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKeyRepository;
import net.pladema.sgx.session.application.port.UserIdStorage;

@ExtendWith(MockitoExtension.class)
class ApiKeyLoginTest extends UnitRepository {

    private ApiKeyLogin login;
	private ApiKeyController apiKeyController;

	@Autowired
    private UserKeyRepository userKeyRepository;
	@Mock
	private UserIdStorage userIdStorage;

    @BeforeEach
    void setUp(){
        var apiKeyStorage = new ApiKeyStorageImpl(userKeyRepository);
        login = new ApiKeyLoginImpl(apiKeyStorage);
		apiKeyController = new ApiKeyController(
				new GenerateUserApiKey(
					userKeyRepository,
					userIdStorage
				),
				new ListUserApiKeys(
					userKeyRepository,
					userIdStorage
				),
				new DeleteUserApiKey(
					userKeyRepository,
					userIdStorage
				)
		);
    }


    @Test
    void loginSuccess() {
        when(userIdStorage.getUserId()).thenReturn(14);
		var generatedKey = apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
        var result = login.execute(generatedKey.apiKey);
        assertEquals(14, result.get().getId());
    }

	@Test
	void validNamesAndlistApiKeys() {
		when(userIdStorage.getUserId()).thenReturn(14);
		apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("second"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("3rd    key"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("four-_-"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("5-k"));

		assertThat(apiKeyController.list()).hasSize(5);

	}

	@Test
	void duplicateByUserNameIsNotAllow() {
		when(userIdStorage.getUserId()).thenReturn(14);
		apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("second"));
		when(userIdStorage.getUserId()).thenReturn(11);
		apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("second"));
		assertThrows(DuplicateKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
		});
	}

	@Test
	void checkInvalidKeyNames() {

		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("  "));
		});

		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto(null));
		});
		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("-"));
		});
		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("-KEY"));
		});
		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("Key?"));
		});
		assertThrows(InvalidKeyNameException.class, () -> {
			apiKeyController.generateApiKey(new GenerateApiKeyDto("?"));
		});
	}

	@Test
	void delete() {
		when(userIdStorage.getUserId()).thenReturn(14);

		apiKeyController.generateApiKey(new GenerateApiKeyDto("first"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("second"));
		apiKeyController.generateApiKey(new GenerateApiKeyDto("1"));
		assertThat(apiKeyController.list()).hasSize(3);

		apiKeyController.deleteApiKey("1");
		assertThat(apiKeyController.list()).hasSize(2);

		apiKeyController.deleteApiKey("doesnt-exists");
		assertThat(apiKeyController.list()).hasSize(2);

		apiKeyController.deleteApiKey("second");
		assertThat(apiKeyController.list()).hasSize(1);
	}

    @Test
    void loginFailed() {
        var result = login.execute("key_de_prueba");
        assertTrue(result.isEmpty());
    }
}