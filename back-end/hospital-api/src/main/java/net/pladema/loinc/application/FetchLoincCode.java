package net.pladema.loinc.application;

import lombok.RequiredArgsConstructor;

import net.pladema.loinc.application.port.LoincCodeStoragePort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FetchLoincCode {
	private final LoincCodeStoragePort loincCodeStoragePort;

	Optional<FetchLoincCodeBo> findByCode(String loincCode) {
		return loincCodeStoragePort.findByCode(loincCode);
	}

	public Map<String, FetchLoincCodeBo> findByCodes(List<String> codes) {
		return loincCodeStoragePort.findByCodes(codes);
	}
}
