package net.pladema.hsi.extensions.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIComponentDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;

class DemoExtensionServiceTest {
	private DemoExtensionService demoExtensionService;
	@BeforeEach
	void setUp() {
		demoExtensionService = new DemoExtensionService();
	}

	@Test
	void getSystemMenu() {
		UIMenuItemDto[] menuItems = demoExtensionService.getSystemMenu();
		assertTrue(menuItems.length == 1);
		Arrays.stream(menuItems).map(
				item -> demoExtensionService.getSystemPage(item.id)
		).forEach(
				page -> {
					assertNotNull(page.type);
					assertNotEquals(page.type, "message");
				}
		);
	}

	@Test
	void getSystemPage() {
		UIPageDto pageDto = demoExtensionService.getSystemPage("sarasa");
		assertEquals(pageDto.type, "message");

		UIComponentDto content = pageDto.content[0];
		assertTrue(content.type == "text");
	}

	@Test
	void getInstitutionMenu() {
		Integer institutionId = 1;
		UIMenuItemDto[] menuItems = demoExtensionService.getInstitutionMenu(institutionId);
		assertTrue(menuItems.length == 1);
		Arrays.stream(menuItems).map(
				item -> demoExtensionService.getInstitutionPage(institutionId, item.id)
		).forEach(
				page -> {
					assertNotNull(page.type);
					assertNotEquals(page.type, "message");
				}
		);
	}

	@Test
	void getPatientMenu() {
		Integer patientId = 1;
		UIMenuItemDto[] menuItems = demoExtensionService.getPatientMenu(patientId);
		assertTrue(menuItems.length == 1);
		Arrays.stream(menuItems).map(
				item -> demoExtensionService.getPatientPage(patientId, item.id)
		).forEach(
				page -> {
					assertNotNull(page.type);
					assertNotEquals(page.type, "message");
				}
		);
	}
}