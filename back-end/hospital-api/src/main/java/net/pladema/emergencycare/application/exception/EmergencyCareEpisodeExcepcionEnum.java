package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public enum EmergencyCareEpisodeExcepcionEnum {
	EPISODE_NOT_FOUND,
	CHANGE_OF_STATE_NOT_VALID,
	EPISODE_ALREADY_DISCHARGED,
	DOCTORS_OFFICE_NOT_AVAILABLE,
	SHOCKROOM_NOT_AVAILABLE,
	BED_NOT_AVAILABLE,
	BLOCKED,
	NOT_FOUND
}
