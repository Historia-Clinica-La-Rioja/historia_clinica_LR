package net.pladema.clinichistory.sipplus.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.sipplus.application.port.SipPlusStorage;

import net.pladema.clinichistory.sipplus.domain.SipPlusUrlDataBo;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetSipPlusUrlData {

	private final SipPlusStorage sipPlusStorage;

	public SipPlusUrlDataBo run() {
		return sipPlusStorage.getUrlData();
	}
}
