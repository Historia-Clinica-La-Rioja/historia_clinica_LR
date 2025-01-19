package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserpersonfromtoken.exception.UserNotExistsException;
import ar.lamansys.sgh.publicapi.userinformation.application.fetchuserprofessionallicensesfromtoken.FetchUserProfessionalLicensesFromToken;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto.FetchUserProfessionalLicensesFromTokenDto;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest.mapper.FetchUserProfessionalLicensesFromTokenMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Tag(name = "PublicApi UserProfessionalLicensesFromToken", description = "Professional licenses from token")
@RequestMapping("/public-api/user/professional/licenses/from-token")
@RestController
public class FetchUserProfessionalLicensesFromTokenController {

	private final FetchUserProfessionalLicensesFromToken fetchUserProfessionalLicensesFromToken;

	@GetMapping
	public @ResponseBody List<FetchUserProfessionalLicensesFromTokenDto> FetchUserProfessionalLicensesFromToken(@RequestHeader("user-token") String userToken)
			throws UserNotExistsException, PublicApiAccessDeniedException {
		log.debug("Input parameters -> userToken {}", userToken);
		var result =
				fetchUserProfessionalLicensesFromToken.run(userToken).stream()
						.map(FetchUserProfessionalLicensesFromTokenMapper::fromBo)
						.collect(Collectors.toList());
		log.debug("FetchUserProfessionalLicensesFromToken {}", result);
		return result;
	}
}
