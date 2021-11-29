package net.pladema.snvs.infrastructure.input.rest.dto;

import lombok.Getter;
import net.pladema.snvs.domain.event.SnvsEventBo;

@Getter
public class SnvsEventDto {

	private final String description;

	private final Integer eventId;

	private final Integer groupEventId;

	public SnvsEventDto(SnvsEventBo snvsEventBo) {
		this.description = snvsEventBo.getDescription();
		this.eventId = snvsEventBo.getEventId();
		this.groupEventId = snvsEventBo.getGroupEventId();
	}
}
