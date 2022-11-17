package net.pladema.hsi.extensions.domain;

import lombok.AllArgsConstructor;
import net.pladema.hsi.extensions.domain.ports.ExtensionComponentStorage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FetchWCDefinitionPaths {

	private final ExtensionComponentStorage extensionComponentStorage;

	public List<ExtensionComponentBo> run() {
		return extensionComponentStorage.fetchDefinitionsPaths();
	}
}
