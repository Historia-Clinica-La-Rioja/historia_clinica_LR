package net.pladema.loinc.application.port;

import net.pladema.loinc.application.FetchLoincCodeBo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LoincCodeStoragePort {
	Optional<FetchLoincCodeBo> findByCode(String loincCode);

    Map<String, FetchLoincCodeBo> findByCodes(List<String> codes);
}
