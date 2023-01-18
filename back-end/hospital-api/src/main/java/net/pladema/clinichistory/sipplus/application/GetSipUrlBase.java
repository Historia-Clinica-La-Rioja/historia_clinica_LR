package net.pladema.clinichistory.sipplus.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.port.SipPlusStorage;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetSipUrlBase {

	private final SipPlusStorage sipPlusStorage;

	public String run() {
		return sipPlusStorage.getUrlBase();
	}
}
