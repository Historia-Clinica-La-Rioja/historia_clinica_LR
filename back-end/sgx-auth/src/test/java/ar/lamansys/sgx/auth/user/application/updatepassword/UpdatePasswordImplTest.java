package ar.lamansys.sgx.auth.user.application.updatepassword;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePasswordImplTest {

    private UpdatePassword updatePassword;

    @Mock
    private UserStorage userStorage;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @BeforeEach
    void setUp() {
        updatePassword = new UpdatePasswordImpl(userStorage, passwordEncryptor);
    }

    @Test
    @DisplayName("Update password success")
    void update_password_success() {
        when(userStorage.getUser("username"))
                .thenReturn(new UserBo(1, "username", true, "password", "salt", "hashAlgorithm", LocalDateTime.of(2020,01,01,10,10), LocalDateTime.of(2020,01,01,01,01)));
        when(passwordEncryptor.encode("password", "salt", "hashAlgorithm"))
                .thenReturn("passwordEncoded");
        updatePassword.execute("username","password");

        ArgumentCaptor<UserBo> userBoArgumentCaptor = ArgumentCaptor.forClass(UserBo.class);
        verify(userStorage, times(1)).update(userBoArgumentCaptor.capture());
        assertThat(userBoArgumentCaptor.getValue().getUsername()).isEqualTo("username");
        assertThat(userBoArgumentCaptor.getValue().getPassword()).isEqualTo("passwordEncoded");

    }

}