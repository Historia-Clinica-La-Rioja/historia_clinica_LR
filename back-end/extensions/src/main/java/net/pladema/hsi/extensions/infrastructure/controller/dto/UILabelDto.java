package net.pladema.hsi.extensions.infrastructure.controller.dto;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UILabelDto {
	@Nullable
	public final String key;
	@Nullable
	public final String text;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	private UILabelDto(
			@JsonProperty("key") String key,
			@JsonProperty("text") String text) {
		this.key = key;
		this.text = text;
	}

	public static UILabelDto fromKey(String key) {
		return new UILabelDto(key, null);
	}

	public static UILabelDto fromText(String text) {
		return new UILabelDto(null, text);
	}

}


