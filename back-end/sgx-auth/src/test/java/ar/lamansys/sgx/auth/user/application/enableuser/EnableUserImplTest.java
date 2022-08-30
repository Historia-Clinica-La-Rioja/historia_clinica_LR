package ar.lamansys.sgx.auth.user.application.enableuser;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.user.service.exceptions.UserStorageEnumException;
import ar.lamansys.sgx.auth.user.domain.user.service.exceptions.UserStorageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnableUserImplTest {

    private EnableUser enableUser;

    @Mock
    private UserStorage userStorage;

    @BeforeEach
    public void setUp() {
        enableUser = new EnableUserImpl(userStorage);
    }

    @Test
    @DisplayName("Enable user success")
    void enableUserSuccess() {
        when(userStorage.getUser("username"))
                .thenReturn(new UserBo(1, "username", true, "password", "salt", "hashAlgoritm", LocalDateTime.of(2020,01,01,10,10), LocalDateTime.of(2020,01,01,01,01)));
        enableUser.execute("username");

        ArgumentCaptor<UserBo> userBoArgumentCaptor = ArgumentCaptor.forClass(UserBo.class);
        verify(userStorage, times(1)).update(userBoArgumentCaptor.capture());
        assertThat(userBoArgumentCaptor.getValue().getUsername()).isEqualTo("username");
        assertThat(userBoArgumentCaptor.getValue().isEnable()).isTrue();

    }

    @Test
    @DisplayName("Enable user not exist fail")
    void enableUserNotExistFail() {
        when(userStorage.getUser("test"))
                .thenThrow(
                        new UserStorageException(UserStorageEnumException.NOT_FOUND_USER, String.format("Usuario con username (%s) inexistente", "test")));
        Exception exception = Assertions.assertThrows(UserStorageException.class, () ->
                enableUser.execute("test")
        );
        String expectedMessage = "Usuario con username (test) inexistente";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);

    }
}