package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken.exception.UserInfoFromTokenAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.PublicUserStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.PublicUserInfoBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FetchUserInfoFromTokenTest {

	@Mock
	private PublicUserStorage publicUserStorage;

	@Mock
	private UserInformationPublicApiPermission userInformationPublicApiPermission;

	@InjectMocks
	private FetchUserInfoFromToken fetchUserInfoFromToken;

	private static final String VALID_TOKEN = "valid-token";
	private static final String INVALID_TOKEN = "invalid-token";
	private static final PublicUserInfoBo USER_INFO = new PublicUserInfoBo(1, "username");

	@BeforeEach
	public void setUp() {
		fetchUserInfoFromToken = new FetchUserInfoFromToken(publicUserStorage, userInformationPublicApiPermission);
	}

	@Test
	void testExecuteWithValidToken() {
		when(userInformationPublicApiPermission.canAccessUserInfoFromToken()).thenReturn(true);

		when(publicUserStorage.fetchUserInfoFromToken(VALID_TOKEN)).thenReturn(Optional.of(USER_INFO));

		Optional<PublicUserInfoBo> result = fetchUserInfoFromToken.execute(VALID_TOKEN);

		assertTrue(result.isPresent());
		assertEquals(USER_INFO, result.get());

		verify(publicUserStorage).fetchUserInfoFromToken(VALID_TOKEN);
	}

	@Test
	void testExecuteWithInvalidToken() {
		when(userInformationPublicApiPermission.canAccessUserInfoFromToken()).thenReturn(true);

		when(publicUserStorage.fetchUserInfoFromToken(INVALID_TOKEN)).thenReturn(Optional.empty());

		Optional<PublicUserInfoBo> result = fetchUserInfoFromToken.execute(INVALID_TOKEN);

		assertFalse(result.isPresent());

		verify(userInformationPublicApiPermission).canAccessUserInfoFromToken();
		verify(publicUserStorage).fetchUserInfoFromToken(INVALID_TOKEN);
	}

	@Test
	void testExecuteWithAccessDenied() {
		when(userInformationPublicApiPermission.canAccessUserInfoFromToken()).thenReturn(false);

		assertThrows(UserInfoFromTokenAccessDeniedException.class, () -> fetchUserInfoFromToken.execute(VALID_TOKEN));

		verify(userInformationPublicApiPermission).canAccessUserInfoFromToken();
	}
}