package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public enum EmergencyCareEpisodeStateExceptionEnum {
	ADMINISTRATIVE_DISCHARGE,
	MEDICAL_DISCHARGE,
	WAITING_ROOM,
	ATTENTION,
	CALLED
}
