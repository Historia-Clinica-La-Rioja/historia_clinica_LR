package net.pladema.hsi.extensions.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;


public class DemoExtensionService implements ExtensionService {
	public static final String[] PUBLIC_MENU_LIST = {"manuales", "components"};
	private static final UIMenuItemDto[] NO_MENU = {};
	private static final UIPageDto INVALID_PAGE = UIPageDto.pageMessage("La página no está disponible");
	@Override
	public UIMenuItemDto[] getSystemMenu() {
		return JsonResourceUtils.readJson(
				"module/demo/system/menu.json",
				new TypeReference<>() {},
				NO_MENU
		);
	}

	@Override
	public UIPageDto getSystemPage(String menuId) {
		return JsonResourceUtils.readJson(
				String.format("module/demo/system/page/%s.json", menuId),
				new TypeReference<>() {},
				INVALID_PAGE
		);
	}

	@Override
	public UIMenuItemDto[] getInstitutionMenu(Integer institutionId) {
		return JsonResourceUtils.readJson(
				String.format("module/demo/institution/%s/menu.json", institutionId),
				new TypeReference<>() {},
				NO_MENU
		);
	}

	@Override
	public UIPageDto getInstitutionPage(Integer institutionId, String menuId) {
		return JsonResourceUtils.readJson(
				String.format("module/demo/institution/%s/page/%s.json", institutionId, menuId),
				new TypeReference<>() {},
				INVALID_PAGE
		);
	}

	@Override
	public UIMenuItemDto[] getPatientMenu(Integer patientId) {
		return JsonResourceUtils.readJson(
				String.format("module/demo/patient/%s/menu.json", patientId),
				new TypeReference<>() {},
				NO_MENU
		);
	}

	@Override
	public UIPageDto getPatientPage(Integer patientId, String menuId) {
		return JsonResourceUtils.readJson(
				String.format("module/demo/patient/%s/page/%s.json", patientId, menuId),
				new TypeReference<>() {},
				INVALID_PAGE
		);
	}
}
