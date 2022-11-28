package net.pladema.hsi.extensions.infrastructure.controller;

import lombok.AllArgsConstructor;
import net.pladema.hsi.extensions.domain.ExtensionComponentBo;
import net.pladema.hsi.extensions.domain.FetchWCDefinitionPaths;
import net.pladema.hsi.extensions.infrastructure.controller.dto.ExtensionComponentDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/extensions")
@Tag(name = "Extensions", description = "Extensions")
public class ExtensionsController {

	private final ExtensionService extensionService;

	private final FetchWCDefinitionPaths fetchWcDefinitionPaths;

	@GetMapping("/menu")
	@ResponseBody
	public UIMenuItemDto[] getSystemMenu() {
		return extensionService.getSystemMenu();
	}

	@GetMapping("/page/{menuId}")
	@ResponseBody
	public UIPageDto getSystemPage(@PathVariable("menuId") String menuId) {
		return extensionService.getSystemPage(menuId);
	}


	@GetMapping("/institution/{institutionId}/menu")
	@ResponseBody
	public UIMenuItemDto[] getInstitutionMenu(@PathVariable("institutionId") Integer institutionId) {
		return extensionService.getInstitutionMenu(institutionId);
	}

	@GetMapping("/institution/{institutionId}/page/{menuId}")
	@ResponseBody
	public UIPageDto getInstitutionPage(@PathVariable("institutionId") Integer institutionId, @PathVariable("menuId") String menuId) {
		return extensionService.getInstitutionPage(institutionId, menuId);
	}

	@GetMapping
	public List<ExtensionComponentDto> getExtensions() {
		return fetchWcDefinitionPaths.run()
				.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}

	private ExtensionComponentDto map (ExtensionComponentBo extensionComponentBo) {
		return new ExtensionComponentDto(extensionComponentBo.path, extensionComponentBo.name);
	}

}
