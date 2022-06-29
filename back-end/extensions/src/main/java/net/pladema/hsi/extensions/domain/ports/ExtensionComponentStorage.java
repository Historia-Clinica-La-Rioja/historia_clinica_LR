package net.pladema.hsi.extensions.domain.ports;

import net.pladema.hsi.extensions.domain.ExtensionComponentBo;

import java.util.List;

public interface ExtensionComponentStorage {

	List<ExtensionComponentBo> fetchDefinitionsPaths();

}
