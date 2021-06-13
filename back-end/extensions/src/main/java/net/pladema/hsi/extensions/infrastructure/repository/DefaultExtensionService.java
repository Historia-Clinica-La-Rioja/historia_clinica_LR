package net.pladema.hsi.extensions.infrastructure.repository;

import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;


public class DefaultExtensionService implements ExtensionService {
	@Override
	public UIMenuItemDto[] getSystemMenu() {
		return new UIMenuItemDto[0];
	}

	@Override
	public UIPageDto getSystemPage(String menuId) {
		return null;
	}

	@Override
	public UIMenuItemDto[] getInstitutionMenu(Integer institutionId) {
		return new UIMenuItemDto[0];
	}

	@Override
	public UIPageDto getInstitutionPage(Integer institutionId, String menuId) {
		return null;
	}

	@Override
	public UIMenuItemDto[] getPatientMenu(Integer patientId) {
		return new UIMenuItemDto[0];
	}

	@Override
	public UIPageDto getPatientPage(Integer patientId, String menuId) {
		return null;
	}
}
