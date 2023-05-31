package net.pladema.digitalsignature.infrastructure.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDigitalSignaturePort;
import lombok.RequiredArgsConstructor;
import net.pladema.digitalsignature.application.generatedigitalsigninglink.GenerateDigitalSigningLink;

@Service
@RequiredArgsConstructor
public class SharedDigitalSignaturePortImpl implements SharedDigitalSignaturePort {

	private final GenerateDigitalSigningLink generateDigitalSigningLink;

	@Override
	public String generateDigitalSigningLink(DigitalSignatureDataDto dto) {
		return generateDigitalSigningLink.run(dto);
	}
}
