package net.pladema.hsi.extensions.infrastructure.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.pladema.hsi.extensions.configuration.plugins.SystemMenuExtensionPlugin;
import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;


public class WrapperExtensionService implements ExtensionService {
	private final ExtensionService extensionService;
	private final List<SystemMenuExtensionPlugin> plugins;

	public WrapperExtensionService(ExtensionService extensionService, List<SystemMenuExtensionPlugin> plugins) {
		this.extensionService = extensionService;
		this.plugins = plugins;
	}

	@Override
	public UIMenuItemDto[] getSystemMenu() {
		return Stream.concat(
				Arrays.stream(extensionService.getSystemMenu()),
				plugins.stream().map(SystemMenuExtensionPlugin::menu)
		)
		.toArray(UIMenuItemDto[]::new);
	}

	@Override
	public UIPageDto getSystemPage(String menuId) {
		return plugins.stream()
				.filter(plugin -> plugin.menu().id.equals(menuId))
				.findFirst()
				.map(SystemMenuExtensionPlugin::page)
				.orElseGet(() -> extensionService.getSystemPage(menuId));
	}

	@Override
	public UIMenuItemDto[] getInstitutionMenu(Integer institutionId) {
		return Arrays.stream(extensionService.getInstitutionMenu(institutionId))
				.toArray(UIMenuItemDto[]::new);
	}

	@Override
	public UIPageDto getInstitutionPage(Integer institutionId, String menuId) {
		return extensionService.getInstitutionPage(institutionId, menuId);
	}

	@Override
	public UIMenuItemDto[] getPatientMenu(Integer patientId) {
		return extensionService.getPatientMenu(patientId);
	}

	@Override
	public UIPageDto getPatientPage(Integer patientId, String menuId) {
		return extensionService.getPatientPage(patientId, menuId);
	}
}
