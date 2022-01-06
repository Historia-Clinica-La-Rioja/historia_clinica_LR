package net.pladema.snvs.domain.event.exceptions;

import lombok.Getter;

@Getter
public enum SnvsEventInfoBoEnumException {
    NULL_GROUP_EVENT_ID, NULL_MANUAL_CLASSIFICATION_ID, NULL_MANUAL_CLASSIFICATIONS, EMPTY_MANUAL_CLASSIFICATIONS, NULL_ENVIRONMENT_ID, NULL_DESCRIPTION, NULL_EVENT_ID
}
