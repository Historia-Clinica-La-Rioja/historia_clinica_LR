package ar.lamansys.sgx.shared.notifications.infrastructure.controller;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.AbstractMasterdataDto;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NotificationTemplateRenderDto extends AbstractMasterdataDto<String> {
	private String body;
	private boolean sendingEnabled;
	public NotificationTemplateRenderDto(String id, String description, String body, boolean isEnabled) {
		this.setId(id);
		this.setDescription(description);
		this.body = body;
		this.sendingEnabled = isEnabled;
	}
}
