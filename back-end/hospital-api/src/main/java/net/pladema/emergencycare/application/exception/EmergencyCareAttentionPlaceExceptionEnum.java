package net.pladema.emergencycare.application.exception;

import lombok.Getter;

@Getter
public enum EmergencyCareAttentionPlaceExceptionEnum {

	ATTENTION_PLACE_NOT_FOUND,
	NO_EPISODE_ASSOCIATED_WITH_BED,
	NO_EPISODE_ASSOCIATED_WITH_SHOCKROOM,
	NO_EPISODE_ASSOCIATED_WITH_DOCTORS_OFFICE,
	NO_ACTIVE_EPISODE_FOUND
}
