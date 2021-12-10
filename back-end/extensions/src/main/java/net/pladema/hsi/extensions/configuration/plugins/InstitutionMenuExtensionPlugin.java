package net.pladema.hsi.extensions.configuration.plugins;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;

public interface InstitutionMenuExtensionPlugin {
	UIMenuItemDto menu();
	UIPageDto page(Integer institutionId);
}
