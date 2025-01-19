package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.FetchUserPersonFromTokenDto;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.FetchUserPersonFromToken;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserNotExistsException;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest.mapper.FetchUserPersonFromTokenMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Tag(name = "PublicApi UserPersonFromToken", description = "Person data from token")
@RequestMapping("/public-api/user/person/from-token")
@RestController
public class FetchUserPersonFromTokenController {

	private final FetchUserPersonFromToken fetchUserPersonFromToken;

	@GetMapping
	public @ResponseBody FetchUserPersonFromTokenDto fetchUserPersonFromToken(@RequestHeader("user-token") String userToken)
	throws UserNotExistsException, PublicApiAccessDeniedException {
		log.debug("Input parameters -> userToken {}", userToken);
		var result = FetchUserPersonFromTokenMapper.fromBo(fetchUserPersonFromToken.run(userToken));
		log.debug("userPersonFromToken {}", result);
		return result;
	}
}
