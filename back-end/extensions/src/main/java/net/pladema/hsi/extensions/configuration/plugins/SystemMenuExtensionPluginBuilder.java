package net.pladema.hsi.extensions.configuration.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;

public class SystemMenuExtensionPluginBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemMenuExtensionPluginBuilder.class);

	public static SystemMenuExtensionPlugin fromResources(String menuId) {
		UIMenuItemDto menu = JsonResourceUtils.readJson(
				String.format("extension/%s/menu.json", menuId),
				new TypeReference<>() {},
				null
		);
		LOGGER.info("Loaded SystemMenu fromResources {} => {}", menuId, menu);
		UIPageDto page = JsonResourceUtils.readJson(
				String.format("extension/%s/page.json", menuId),
				new TypeReference<>() {},
				null
		);
		return new SystemMenuExtensionPlugin() {

			@Override
			public UIMenuItemDto menu() {
				return menu;
			}

			@Override
			public UIPageDto page() {
				return page;
			}
		};
	}
}
