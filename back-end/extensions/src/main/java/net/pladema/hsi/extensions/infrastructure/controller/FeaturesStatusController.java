package net.pladema.hsi.extensions.infrastructure.controller;

import net.pladema.hsi.extensions.configuration.features.FeatureProperty;
import net.pladema.hsi.extensions.configuration.features.FeatureStatus;
import net.pladema.hsi.extensions.configuration.features.FeatureStatusService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIComponentDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UILabelDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static net.pladema.hsi.extensions.infrastructure.controller.dto.UILabelDto.fromKey;


@RestController
@RequestMapping("/extensions/features")
//@Tag(name = "Dashboards", description = "Dashboards")
public class FeaturesStatusController {
	private final static UIComponentDto DIVIDER = new UIComponentDto("divider", Collections.emptyMap());
	private final List<FeatureStatusService> featureStatusService;

	public FeaturesStatusController(List<FeatureStatusService> featureStatusService) {
		this.featureStatusService = featureStatusService;
	}

	@GetMapping
	@ResponseBody
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public UIPageDto status() {
		return new UIPageDto(
				"components",
				content()
		);
	}

	private UIComponentDto[] content() {
		return featureStatusService.stream()
				.map(FeatureStatusService::status)
				.map(this::buildUIComponent)
				.toArray(UIComponentDto[]::new);
	}

	private UIComponentDto buildUIComponent(FeatureStatus featureStatusDto) {
		UILabelDto title = fromKey(String.format("%s.TITLE", featureStatusDto.prefix));

		UIComponentDto[] content = Stream.concat(
				propertiesToUIComponent(featureStatusDto.prefix, featureStatusDto.properties),
				dataToUIComponent(featureStatusDto.data)
		).toArray(UIComponentDto[]::new);

		return new UIComponentDto(
			"card",
				Map.of(
						"title", title,
						"content", content
				)
		);
	}

	private Stream<UIComponentDto> dataToUIComponent(Map<String, Object> data) {
		if (data.isEmpty()) {
			return Stream.empty();
		}
		return Stream.of(
				DIVIDER,
				new UIComponentDto("json", Map.of(
				"content", data
				))
		);
	}

	private static Stream<UIComponentDto> propertiesToUIComponent(
			String prefix,
			List<FeatureProperty> properties
	) {
		return properties.stream()
				.map(featureProperty -> propertyToUIComponent(prefix, featureProperty));
	}

	private static UIComponentDto propertyToUIComponent(String prefix, FeatureProperty property) {
		return new UIComponentDto(
				"typography",
				Map.of(
						"className", "caption",
						"value", String.format("%s.%s = %s", prefix, property.name, property.value)
				)
		);
	}

}
