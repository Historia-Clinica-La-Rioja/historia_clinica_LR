package net.pladema.parameterizedform.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetFormsByStatus {

	private final ParameterizedFormStorage parameterizedFormStorage;

	public Page<ParameterizedForm> run(List<Short> statusIds, String name, Pageable pageable) {
		log.debug("Input parameters -> statusIds {}, name {}, pageable {}", statusIds, name, pageable);
		Page<ParameterizedForm> result = parameterizedFormStorage.filterByStatusIdAndNameIn(statusIds, name, pageable);
		log.debug("Output -> {}", result);
		return result;
	}
}
