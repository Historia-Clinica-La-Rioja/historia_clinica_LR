package net.pladema.hsi.extensions.infrastructure.repository;

import lombok.AllArgsConstructor;
import net.pladema.hsi.extensions.domain.ExtensionComponentBo;
import net.pladema.hsi.extensions.domain.ports.ExtensionComponentStorage;

import net.pladema.hsi.extensions.infrastructure.repository.entities.ExtensionDefinitionPath;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExtensionComponentStorageImpl implements ExtensionComponentStorage {

	private final WcDefinitionPathsRepository wcDefinitionPathsRepository;

	@Override
	public List<ExtensionComponentBo> fetchDefinitionsPaths() {
		return wcDefinitionPathsRepository.findAll()
				.stream()
				.map(this::toExtensionComponentBo)
				.collect(Collectors.toList());

	}

	private ExtensionComponentBo toExtensionComponentBo(ExtensionDefinitionPath e) {
		return new ExtensionComponentBo(e.getPath(),e.getName());
	}
}
