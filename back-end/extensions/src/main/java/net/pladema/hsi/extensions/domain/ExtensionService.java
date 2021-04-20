package net.pladema.hsi.extensions.domain;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;

public interface ExtensionService {
	UIMenuItemDto[] getSystemMenu();
	UIPageDto getSystemPage(String menuId);

	UIMenuItemDto[] getInstitutionMenu(Integer institutionId);
	UIPageDto getInstitutionPage(Integer institutionId, String menuId);

	UIMenuItemDto[] getPatientMenu(Integer patientId);
	UIPageDto getPatientPage(Integer patientId, String menuId);
}
