package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.FetchUserPersonFromTokenDto;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.FetchUserPersonFromToken;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserInformationAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserNotExistsException;
import ar.lamansys.sgh.publicapi.userinformation.application.port.out.FetchUserPersonFromTokenStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

@ExtendWith(MockitoExtension.class)
public class FetchUserPersonFromTokenControllerApplicationTest {

	private static final String TOKEN = "token";

	private FetchUserPersonFromTokenController fetchUserPersonFromTokenController;

	@Mock
	private FetchUserPersonFromTokenStorage fetchUserPersonFromTokenStorage;

	@Mock
	private UserInformationPublicApiPermission userInformationPublicApiPermission;

	@BeforeEach
	public void setup() {
		FetchUserPersonFromToken fetchUserPersonFromToken = new FetchUserPersonFromToken(fetchUserPersonFromTokenStorage, userInformationPublicApiPermission);
		this.fetchUserPersonFromTokenController = new FetchUserPersonFromTokenController(fetchUserPersonFromToken);
	}

	@Test
	void successFetchUserPersonFromToken() {
		var personInfo = fabricatePerson("test",1);
		var userPersonFromToken = personIsFetched(personInfo);
		shouldBeSamePerson(personInfo, userPersonFromToken);
	}

	@Test
	void failFetchUserPersonFromTokenUserNotExists() {
		allowAccessPermission(true);
		userIsNotFound();
		TestUtils.shouldThrow(UserNotExistsException.class,
				() -> fetchUserPersonFromTokenController.fetchUserPersonFromToken(TOKEN));
	}

	@Test
	void failFetchUserPersonFromTokenUserInformationAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(UserInformationAccessDeniedException.class,
				() -> fetchUserPersonFromTokenController.fetchUserPersonFromToken(TOKEN));
	}

	private void shouldBeSamePerson(FetchUserPersonFromTokenBo personInfo, FetchUserPersonFromTokenDto userPersonFromToken) {
		Assertions.assertEquals(userPersonFromToken.getId(), personInfo.getId());
		Assertions.assertEquals(userPersonFromToken.getSub(), personInfo.getSub());
		Assertions.assertEquals(userPersonFromToken.getEmail(), personInfo.getEmail());
		Assertions.assertEquals(userPersonFromToken.getGivenName(), personInfo.getGivenName());
		Assertions.assertEquals(userPersonFromToken.getFamilyName(), personInfo.getFamilyName());
		Assertions.assertEquals(userPersonFromToken.getCuil(), personInfo.getCuil());
		Assertions.assertEquals(userPersonFromToken.getIdentificationType(), personInfo.getIdentificationType());
		Assertions.assertEquals(userPersonFromToken.getIdentificationNumber(), personInfo.getIdentificationNumber());
		Assertions.assertEquals(userPersonFromToken.getGender(), personInfo.getGender());
	}

	private FetchUserPersonFromTokenBo fabricatePerson(String name, Integer id) {
		return new FetchUserPersonFromTokenBo(
				id,
				name + "@example.com",
				name + "@mail.com", name,
				name + "_FamilyName",
				name + "_cuil",
				name + "_idType",
				name + "_idNumber",
				"F");
	}

	private FetchUserPersonFromTokenDto personIsFetched(FetchUserPersonFromTokenBo personInfo) {
		when(userInformationPublicApiPermission.canAccess()).thenReturn(true);
		when(fetchUserPersonFromTokenStorage.getUserPersonFromToken(any())).
				thenReturn(Optional.of(personInfo));
		return fetchUserPersonFromTokenController.fetchUserPersonFromToken(TOKEN);
	}

	private void userIsNotFound() {
		when(fetchUserPersonFromTokenStorage.getUserPersonFromToken(any())).thenReturn(Optional.empty());
	}

	private void allowAccessPermission(boolean canAccess) {
		when(userInformationPublicApiPermission.canAccess()).thenReturn(canAccess);
	}
}
