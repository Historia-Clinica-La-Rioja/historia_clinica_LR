package ar.lamansys.sgx.auth.apiKey.application.login;

import ar.lamansys.sgx.auth.UnitRepository;
import ar.lamansys.sgx.auth.apiKey.domain.ApiKeyStorage;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.ApiKeyStorageImpl;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKey;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(showSql = false)
class ApiKeyLoginTest extends UnitRepository {

    private ApiKeyLogin login;

    @Mock
    private UserKeyRepository userKeyRepository;

    @BeforeEach
    void setUp(){
        var apiKeyStorage = new ApiKeyStorageImpl(userKeyRepository);
        login = new ApiKeyLoginImpl(apiKeyStorage);
    }


    @Test
    void loginSuccess() {
        when(userKeyRepository.getUserKeyByKey(any())).thenReturn(Optional.of(new UserKey(14, "key_de_prueba")));
        var result = login.execute("key_de_prueba");
        assertEquals(14, result.get().getId());
    }

    @Test
    void loginFailed() {
        when(userKeyRepository.getUserKeyByKey(any())).thenReturn(Optional.empty());
        var result = login.execute("key_de_prueba");
        assertTrue(result.isEmpty());
    }
}