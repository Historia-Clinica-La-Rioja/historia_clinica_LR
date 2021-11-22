package net.pladema.hsi.extensions.domain;

import net.pladema.hsi.extensions.domain.exception.ExtensionException;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;

public interface ExtensionService {
	UIMenuItemDto[] getSystemMenu() throws ExtensionException;
	UIPageDto getSystemPage(String menuId) throws ExtensionException;

	UIMenuItemDto[] getInstitutionMenu(Integer institutionId) throws ExtensionException;
	UIPageDto getInstitutionPage(Integer institutionId, String menuId) throws ExtensionException;

	UIMenuItemDto[] getPatientMenu(Integer patientId) throws ExtensionException;
	UIPageDto getPatientPage(Integer patientId, String menuId) throws ExtensionException;
}
