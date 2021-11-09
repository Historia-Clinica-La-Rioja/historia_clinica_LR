package net.pladema.hsi.extensions.infrastructure.controller.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UIComponentDto {
	public final String type;
	public final Map<String, Object> args;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public UIComponentDto(
			@JsonProperty("type") String type,
			@JsonProperty("args") Map<String, Object> args
	) {
		this.type = type;
		this.args = args;
	}
}
