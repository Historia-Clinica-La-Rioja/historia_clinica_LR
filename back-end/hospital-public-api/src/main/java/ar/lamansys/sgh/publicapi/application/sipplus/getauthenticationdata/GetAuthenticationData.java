package ar.lamansys.sgh.publicapi.application.sipplus.getauthenticationdata;

import ar.lamansys.sgh.publicapi.application.port.out.PublicSipPlusStorage;
import ar.lamansys.sgh.publicapi.domain.sipplus.EmbeddedAuthenticationDataBo;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAuthenticationData {

	private final PublicSipPlusStorage publicSipPlusStorage;

	public EmbeddedAuthenticationDataBo run(String accessData) {
		log.debug("Input parameter -> accessData {}", accessData);
		return publicSipPlusStorage.getDataForAuthentication(accessData);
	}

}
