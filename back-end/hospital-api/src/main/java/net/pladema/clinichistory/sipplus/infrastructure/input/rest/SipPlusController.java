package net.pladema.clinichistory.sipplus.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.GetSipUrlBase;

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

	private final GetSipUrlBase getSipUrlBase;

	@GetMapping(value = "/url-base")
	public ResponseEntity<String> getSipPlusURLBase() {
		String url = getSipUrlBase.run();
		log.debug("Get sip plus url ", url);
		return ResponseEntity.ok().body(url);
	}
}
