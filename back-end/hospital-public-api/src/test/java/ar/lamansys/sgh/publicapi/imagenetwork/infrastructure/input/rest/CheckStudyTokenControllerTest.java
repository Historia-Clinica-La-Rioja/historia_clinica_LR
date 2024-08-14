package ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.imagenetwork.application.check.CheckStudyPermission;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.BadStudyTokenException;
import ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.rest.dto.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CheckStudyTokenControllerTest {

	@Mock
	private CheckStudyPermission checkStudyPermission;

	private CheckStudyTokenController controller;

	@BeforeEach
	public void setUp() {
		controller = new CheckStudyTokenController(checkStudyPermission);
	}

	@Test
	public void testCheckPermissions() throws BadStudyTokenException {
		String studyInstanceUID = "studyInstanceUID";
		String tokenStudy = "tokenStudy";

		String expectedToken = "validated-token";

		when(checkStudyPermission.run(studyInstanceUID, tokenStudy)).thenReturn(expectedToken);

		TokenDto result = controller.checkPermissions(studyInstanceUID, tokenStudy);

		assertEquals("Expected token to match", expectedToken, result.getToken());

		result.setToken("new-token");
		assertEquals("Expected 'token' to be 'new-token'", "new-token", result.getToken());
		assertEquals("Expected toString to match", "TokenDto(token=new-token)", result.toString());

		TokenDto tokenDto = new TokenDto("another-token");
		assertEquals("Expected token to be 'another-token'", "another-token", tokenDto.getToken());

		TokenDto emptyTokenDto = new TokenDto();
		assertEquals("Expected token to be null", null, emptyTokenDto.getToken());

		emptyTokenDto.setToken("set-token");
		assertEquals("Expected token to be 'set-token'", "set-token", emptyTokenDto.getToken());
	}
}
