package net.pladema.loinc.infrastructure.output;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.loinc.application.FetchLoincCodeBo;
import net.pladema.loinc.application.port.LoincCodeStoragePort;
import net.pladema.loinc.infrastructure.output.entity.LoincCode;
import net.pladema.loinc.infrastructure.output.repository.LoincCodeRepository;

@RequiredArgsConstructor
@Service
public class LoincCodeStoragePortImpl implements LoincCodeStoragePort {

	private final LoincCodeRepository loincCodeRepository;

	@Override
	public Optional<FetchLoincCodeBo> findByCode(String loincCode) {
		return loincCodeRepository
		.findByCode(loincCode)
		.map(this::toFetchLoincCodeBo);
	}

	@Override
	public Map<String, FetchLoincCodeBo> findByCodes(List<String> codes) {
		return loincCodeRepository
		.findByCodeIn(codes)
		.stream()
		.collect(Collectors.toMap(LoincCode::getCode, this::toFetchLoincCodeBo));
	}

	@Override
	public Optional<String> getDescriptionById(Integer id){
		return loincCodeRepository.findById(id)
				.map(lc -> lc.getCustomDisplayName() != null ?  lc.getCustomDisplayName() : lc.getDescription());
	}

	private FetchLoincCodeBo toFetchLoincCodeBo(LoincCode entity) {
		return new FetchLoincCodeBo(
				entity.getId(),
				entity.getDescription(),
				entity.getCode(),
				entity.getDisplayName(),
				entity.getCustomDisplayName()
		);
	}
}
