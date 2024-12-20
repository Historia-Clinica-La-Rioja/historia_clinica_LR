package net.pladema.imagenetwork.derivedstudies.domain.exception;

import lombok.Getter;

@Getter
public enum EMoveStudiesException {
    NO_PACS_AVAILABLE,
    IMAGE_ID_NOT_DEFINED,
    ORCHESTRATOR_NOT_FOUND,
}
