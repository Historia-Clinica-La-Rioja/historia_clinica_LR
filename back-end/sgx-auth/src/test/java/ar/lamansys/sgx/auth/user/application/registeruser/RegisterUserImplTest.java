package ar.lamansys.sgx.auth.user.application.registeruser;

import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class RegisterUserImplTest {

    private RegisterUser registerUser;

    @Mock
    private UserStorage userStorage;

    @Mock
    private OAuthUserManagementStorage oAuthUserManagementStorage;

    @Mock
    private  PasswordEncryptor passwordEncryptor;

    @BeforeEach
    public void setUp() {
        registerUser = new RegisterUserImpl(userStorage,
                oAuthUserManagementStorage,
                "^[A-Za-z]\\\\w{5,29}$}",
                passwordEncryptor,
                "PRUEBA");
    }

    @Test
    @DisplayName("Username validations")
    void usernameValidations() {
        Exception exception = Assertions.assertThrows(RegisterUserException.class, () ->
                registerUser.execute(null, "EMAIL", "PASSWORD")
        );
        String expectedMessage = "El username es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);


        exception = Assertions.assertThrows(RegisterUserException.class, () ->
                registerUser.execute("12USERNAME", "EMAIL", "PASSWORD")
        );
        expectedMessage = "El username 12USERNAME no cumple con el patrón ^[A-Za-z]\\\\w{5,29}$} obligatorio";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}