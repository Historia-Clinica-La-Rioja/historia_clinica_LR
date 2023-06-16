package ar.lamansys.sgh.publicapi.infrastructure.input.rest.sipplus;

import ar.lamansys.sgh.publicapi.application.sipplus.getauthenticationdata.GetAuthenticationData;
import ar.lamansys.sgh.publicapi.domain.sipplus.EmbeddedAuthenticationDataBo;
import ar.lamansys.sgh.publicapi.domain.sipplus.SipPlusUserBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus.EmbeddedAuthenticationDataDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus.SipPlusCoordinatesDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus.SipPlusInstitutionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus.SipPlusUserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RequestMapping("/public-api/sip-plus")
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "PublicApi Sip", description = "Sip Plus")
public class SipPlusPublicController {

	private final GetAuthenticationData getAuthenticationData;

	@GetMapping(value = "/embedded-authentication/{accessData}")
	public EmbeddedAuthenticationDataDto getAuthenticationData(@PathVariable String accessData) {
		log.debug("Input parameter -> accessData {} ", accessData);
		EmbeddedAuthenticationDataDto result = mapToEmbeddedAuthenticationDataDto(getAuthenticationData.run(accessData));
		log.debug("Get embedded authentication data {}", result);
		return result;
	}

	private EmbeddedAuthenticationDataDto mapToEmbeddedAuthenticationDataDto(EmbeddedAuthenticationDataBo embeddedAuthenticationDataBo) {
		return EmbeddedAuthenticationDataDto.builder()
				.user(mapToSipPlusUserDto(embeddedAuthenticationDataBo.getUser()))
				.embedCoordinates(new SipPlusCoordinatesDto(embeddedAuthenticationDataBo.getEmbedCoordinates()))
				.institution(new SipPlusInstitutionDto(embeddedAuthenticationDataBo.getInstitution()))
				.build();
	}

	private SipPlusUserDto mapToSipPlusUserDto(SipPlusUserBo sipPlusUserBo) {
		return SipPlusUserDto.builder()
				.id(sipPlusUserBo.getId())
				.userName(sipPlusUserBo.getUserName())
				.fullName(sipPlusUserBo.getFullName())
				.countryId(sipPlusUserBo.getCountryId())
				.roles(sipPlusUserBo.getRoles())
				.institutions(sipPlusUserBo.getInstitutions().stream().map(SipPlusInstitutionDto::new)
						.collect(Collectors.toList()))
				.readableInstitutions(sipPlusUserBo.getInstitutions().stream().map(SipPlusInstitutionDto::new)
						.collect(Collectors.toList()))
				.build();
	}


}
