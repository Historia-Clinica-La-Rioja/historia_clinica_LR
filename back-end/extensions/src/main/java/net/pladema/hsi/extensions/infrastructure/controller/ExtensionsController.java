package net.pladema.hsi.extensions.infrastructure.controller;

import lombok.AllArgsConstructor;
import net.pladema.hsi.extensions.domain.ExtensionComponentBo;
import net.pladema.hsi.extensions.domain.FetchWCDefinitionPaths;
import net.pladema.hsi.extensions.infrastructure.controller.dto.ExtensionComponentDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/extensions")
@Tag(name = "Extensions", description = "Extensions")
public class ExtensionsController {

	private final FetchWCDefinitionPaths fetchWcDefinitionPaths;


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
