package net.pladema.hsi.extensions.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UIMenuItemDto {
	public final UILabelDto label;
	public final String icon;
	public final String id;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public UIMenuItemDto(
			@JsonProperty("label") UILabelDto label,
			@JsonProperty("icon") String icon,
			@JsonProperty("id") String id) {
		this.label = label;
		this.icon = icon;
		this.id = id;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("UIMenuItemDto{");
		sb.append("id='").append(id).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
