package net.pladema.hsi.extensions.infrastructure.controller.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UIPageDto {
	public final String type;
	public final UIComponentDto[] content;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public UIPageDto(
			@JsonProperty("type") String type,
			@JsonProperty("content") UIComponentDto[] content) {
		this.type = type;
		this.content = content;
	}

	public static UIPageDto pageMessage(String textMessage) {
		return new UIPageDto(
				"message",
				new UIComponentDto[]{new UIComponentDto("text", Map.of("text", textMessage))}

		);
	}

}
