package ar.lamansys.sgh.publicapi.infrastructure.input.rest.user;

import ar.lamansys.sgh.publicapi.application.fetchuserauthoritiesfromtoken.FetchUserAuthoritiesFromToken;
import ar.lamansys.sgh.publicapi.application.fetchuserinfofromtoken.FetchUserInfoFromToken;
import ar.lamansys.sgh.publicapi.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.domain.user.PublicUserInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.user.dto.PublicAuthorityDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.user.dto.PublicUserInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/public-api/user")
@Tag(name = "Public Api", description = "User")
public class UserPublicController {

	private final FetchUserInfoFromToken fetchUserInfoFromToken;

	private final FetchUserAuthoritiesFromToken fetchUserAuthoritiesFromToken;

	public UserPublicController(FetchUserInfoFromToken fetchUserInfoFromToken,
								FetchUserAuthoritiesFromToken fetchUserAuthoritiesFromToken) {
		this.fetchUserInfoFromToken = fetchUserInfoFromToken;
		this.fetchUserAuthoritiesFromToken = fetchUserAuthoritiesFromToken;
	}

	@GetMapping(value = "/info/from-token")
	public PublicUserInfoDto fetchUserInfoFromToken(@RequestHeader("user-token") String userToken) {
		var result = fetchUserInfoFromToken.execute(userToken)
				.map(this::mapToDto)
				.orElse(null);
		log.debug("Output -> {}", result);
		return result;
	}

	@GetMapping(value = "/permissions/from-token")
	public List<PublicAuthorityDto> fetchPermissionsFromToken(@RequestHeader("user-token") String userToken) {
		var result = fetchUserAuthoritiesFromToken.execute(userToken)
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private PublicUserInfoDto mapToDto(PublicUserInfoBo publicUserInfoBo) {
		return new PublicUserInfoDto(publicUserInfoBo.getId(), publicUserInfoBo.getUsername());
	}
	private PublicAuthorityDto mapToDto(PublicAuthorityBo authorityBo) {
		return new PublicAuthorityDto(authorityBo.getId(), authorityBo.getInstitution(), authorityBo.getDescription());
	}
}
