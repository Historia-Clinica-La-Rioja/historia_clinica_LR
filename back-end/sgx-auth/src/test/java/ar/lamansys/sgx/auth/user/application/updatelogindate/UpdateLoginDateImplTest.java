package ar.lamansys.sgx.auth.user.application.updatelogindate;

import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
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
class UpdateLoginDateImplTest {

    private UpdateLoginDate updateLoginDate;

    @Mock
    private UserStorage userStorage;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @BeforeEach
    void setUp(){
        updateLoginDate = new UpdateLoginDateImpl(userStorage, dateTimeProvider);
    }

    @Test
    @DisplayName("Update login date success")
    void updateLoginDateSuccess() {
        when(userStorage.getUser(anyString())).thenReturn(new UserBo(1,"USERNAME", true, "PASSWORD_ENCRIPTED", "SALT", "HASH", LocalDateTime.of(2020,01,15,15,16), LocalDateTime.of(2020,01,01,01,01)));
        when(dateTimeProvider.nowDateTime()).thenReturn(LocalDateTime.of(2020,01,15,15,16));
        updateLoginDate.execute("USERNAME");

        ArgumentCaptor<UserBo> userBoArgumentCaptor = ArgumentCaptor.forClass(UserBo.class);
        verify(userStorage, times(1)).update(userBoArgumentCaptor.capture());
        assertThat(userBoArgumentCaptor.getValue().getUsername()).isEqualTo("USERNAME");
        assertThat(userBoArgumentCaptor.getValue().getLastLogin()).isEqualTo(LocalDateTime.of(2020,01,15,15,16));

    }
}
