package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserauthoritiesfromtoken.FetchUserAuthoritiesFromToken;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserinfofromtoken.FetchUserInfoFromToken;
import ar.lamansys.sgh.publicapi.userinformation.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.userinformation.domain.PublicUserInfoBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.PublicAuthorityDto;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.PublicUserInfoDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPublicControllerTest {

	@Mock
	private FetchUserInfoFromToken fetchUserInfoFromToken;

	@Mock
	private FetchUserAuthoritiesFromToken fetchUserAuthoritiesFromToken;

	@InjectMocks
	private UserPublicController userPublicController;

	@BeforeEach
	public void setUp() {
		userPublicController = new UserPublicController(fetchUserInfoFromToken, fetchUserAuthoritiesFromToken);
	}

	@Test
	void testFetchUserInfoFromToken() {
		String userToken = "token";
		PublicUserInfoBo publicUserInfoBo = new PublicUserInfoBo(1, "username");
		when(fetchUserInfoFromToken.execute(userToken)).thenReturn(Optional.of(publicUserInfoBo));

		PublicUserInfoDto result = userPublicController.fetchUserInfoFromToken(userToken);

		assertEquals(publicUserInfoBo.getId(), result.getId());
		assertEquals(publicUserInfoBo.getUsername(), result.getUsername());
	}

	@Test
	void testFetchUserAuthoritiesFromToken() {
		String userToken = "token";
		PublicAuthorityBo authorityBo = new PublicAuthorityBo((short) 1, 1, "description");
		when(fetchUserAuthoritiesFromToken.execute(userToken)).thenReturn(List.of(authorityBo));

		List<PublicAuthorityDto> result = userPublicController.fetchPermissionsFromToken(userToken);

		assertEquals(1, result.size());
		assertEquals(authorityBo.getId(), result.get(0).getId());
		assertEquals(authorityBo.getInstitution(), result.get(0).getInstitution());
		assertEquals(authorityBo.getDescription(), result.get(0).getDescription());
	}
}