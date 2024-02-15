package net.pladema.digitalsignature.application.generatedigitalsigninglink;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.digitalsignature.application.port.DigitalSignatureWSStorage;

@RequiredArgsConstructor
@Service
@Slf4j
public class GenerateDigitalSigningLink {

	private final DigitalSignatureWSStorage digitalSignatureWSStorage;

	public String run(DigitalSignatureDataDto digitalSignatureDataDto) {
		log.debug("Documents to sign -> {}", digitalSignatureDataDto.getDocuments());
		String link = digitalSignatureWSStorage.generateDigitalSigningLink(digitalSignatureDataDto);
		log.debug("Output result -> {}", link);
		return link;
	}


}
