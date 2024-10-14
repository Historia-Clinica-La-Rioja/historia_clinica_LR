package ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken.exception.UserAuthoritiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.PublicUserStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FetchUserAuthoritiesFromTokenTest {

	@Mock
	private PublicUserStorage publicUserStorage;

	@Mock
	private UserInformationPublicApiPermission userInformationPublicApiPermission;

	@InjectMocks
	private FetchUserAuthoritiesFromToken fetchUserAuthoritiesFromToken;

	private static final String VALID_TOKEN = "valid-token";
	private static final String INVALID_TOKEN = "invalid-token";
	private static final List<PublicAuthorityBo> AUTHORITIES = List.of(
			new PublicAuthorityBo((short) 1, 1, "ROLE_ADMIN"),
			new PublicAuthorityBo((short) 2, 1, "ROLE_USER")
	);

	@BeforeEach
	public void setUp() {
		fetchUserAuthoritiesFromToken = new FetchUserAuthoritiesFromToken(publicUserStorage, userInformationPublicApiPermission);
	}

	@Test
	void testExecuteValidToken() {
		when(userInformationPublicApiPermission.canAccessUserAuthoritiesFromToken()).thenReturn(true);
		when(publicUserStorage.fetchRolesFromToken(VALID_TOKEN)).thenReturn(AUTHORITIES);

		List<PublicAuthorityBo> result = fetchUserAuthoritiesFromToken.execute(VALID_TOKEN);

		assertNotNull(result);
		assertEquals(AUTHORITIES, result);

		verify(userInformationPublicApiPermission).canAccessUserAuthoritiesFromToken();
		verify(publicUserStorage).fetchRolesFromToken(VALID_TOKEN);
	}

	@Test
	void testExecuteInvalidToken() {
		when(userInformationPublicApiPermission.canAccessUserAuthoritiesFromToken()).thenReturn(true);
		when(publicUserStorage.fetchRolesFromToken(INVALID_TOKEN)).thenReturn(Collections.emptyList());

		List<PublicAuthorityBo> result = fetchUserAuthoritiesFromToken.execute(INVALID_TOKEN);

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(userInformationPublicApiPermission).canAccessUserAuthoritiesFromToken();
		verify(publicUserStorage).fetchRolesFromToken(INVALID_TOKEN);
	}

	@Test
	void testExecuteAccessDenied() {
		when(userInformationPublicApiPermission.canAccessUserAuthoritiesFromToken()).thenReturn(false);

		assertThrows(UserAuthoritiesAccessDeniedException.class, () -> fetchUserAuthoritiesFromToken.execute(VALID_TOKEN));

		verify(userInformationPublicApiPermission).canAccessUserAuthoritiesFromToken();
		verify(publicUserStorage, never()).fetchRolesFromToken(anyString());
	}
}