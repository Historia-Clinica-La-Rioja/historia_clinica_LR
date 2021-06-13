package net.pladema.hsi.extensions.infrastructure.controller.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UIComponentDto {
	public final String type;
	public final Map<String, Object> args;
	public final UIComponentDto[] children;
	public final UIComponentDto[] actions;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public UIComponentDto(
			@JsonProperty("type") String type,
			@JsonProperty("args") Map<String, Object> args,
			@JsonProperty("children") UIComponentDto[] children,
			@JsonProperty("actions") UIComponentDto[] actions
	) {
		this.type = type;
		this.args = args;
		this.children = children;
		this.actions = actions;
	}
}
