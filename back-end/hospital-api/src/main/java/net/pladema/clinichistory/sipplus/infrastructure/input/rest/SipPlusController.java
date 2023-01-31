package net.pladema.clinichistory.sipplus.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.GetSipPlusUrlData;

import net.pladema.clinichistory.sipplus.domain.SipPlusUrlDataBo;

import net.pladema.clinichistory.sipplus.infrastructure.input.rest.dto.SipPlusUrlDataDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/sip-plus")
@Tag(name = "Sip Plus", description = "Sip Plus")
@Slf4j
@RequiredArgsConstructor
@RestController
public class SipPlusController {

	private final GetSipPlusUrlData getSipPlusUrlData;

	@GetMapping(value = "/url-info")
	public ResponseEntity<SipPlusUrlDataDto> getUrlData() {
		SipPlusUrlDataDto urlData = mapToDto(getSipPlusUrlData.run());
		log.debug("Get data to create sip embedded session url ", urlData);
		return ResponseEntity.ok().body(urlData);
	}

	private SipPlusUrlDataDto mapToDto(SipPlusUrlDataBo sipPlusUrlDataBo) {
		return SipPlusUrlDataDto.builder()
				.token(sipPlusUrlDataBo.getToken())
				.urlBase(sipPlusUrlDataBo.getUrlBase())
				.build();
	}
}
