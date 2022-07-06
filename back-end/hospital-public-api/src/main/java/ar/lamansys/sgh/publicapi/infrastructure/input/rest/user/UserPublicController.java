package ar.lamansys.sgh.publicapi.infrastructure.input.rest.user;

import ar.lamansys.sgh.publicapi.application.fetchuserinfofromtoken.FetchUserInfoFromToken;
import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.user.dto.PublicAuthorityDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.user.dto.PublicUserInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/public-api/user")
@Tag(name = "Public Api", description = "User")
public class UserPublicController {

	private final FetchUserInfoFromToken fetchUserInfoFromToken;

	public UserPublicController(FetchUserInfoFromToken fetchUserInfoFromToken) {
		this.fetchUserInfoFromToken = fetchUserInfoFromToken;
	}

	@GetMapping(value = "/info/from-token")
	public PublicUserInfoDto fetchCompleteInfo(@RequestHeader("user-token") String userToken) {
		var result = fetchUserInfoFromToken.execute(userToken)
				.map(this::mapToDto)
				.orElse(null);
		log.debug("Output -> {}", result);
		return result;
	}

	private PublicUserInfoDto mapToDto(PublicUserInfoBo publicUserInfoBo) {
		return new PublicUserInfoDto(publicUserInfoBo.getId(), publicUserInfoBo.getUsername(),
				publicUserInfoBo.getRoles().stream()
						.map(pbo -> new PublicAuthorityDto(pbo.getId(), pbo.getInstitution(), pbo.getDescription()))
						.collect(Collectors.toList()));
	}

}
