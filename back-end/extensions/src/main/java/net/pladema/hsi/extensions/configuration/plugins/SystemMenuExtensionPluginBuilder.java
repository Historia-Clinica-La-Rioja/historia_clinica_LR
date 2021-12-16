package net.pladema.hsi.extensions.configuration.plugins;

import com.fasterxml.jackson.core.type.TypeReference;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;

public class SystemMenuExtensionPluginBuilder {

	public static SystemMenuExtensionPlugin fromResources(String menuId) {
		UIMenuItemDto menu = JsonResourceUtils.readJson(
				String.format("classpath:extension/%s/menu.json", menuId),
				new TypeReference<>() {},
				null
		);
		UIPageDto page = JsonResourceUtils.readJson(
				String.format("classpath:extension/%s/page.json", menuId),
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
