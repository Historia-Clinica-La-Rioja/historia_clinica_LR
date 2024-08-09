package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgh.clinichistory.domain.document.exceptions.DocumentException;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum EPreviousDocumentStatus {

    DRAFT("445667001") {
        @Override
        public EPreviousDocumentStatus getNext() {
            return DRAFT_DISCARDED;
        }
    },
    DRAFT_DISCARDED("260385009") {
        @Override
        public EPreviousDocumentStatus getNext() {
            return null;
        }
    },
    ERROR("723510000") {
        @Override
        public EPreviousDocumentStatus getNext() {
            return null;
        }
    },
    FINAL("445665009") {
        @Override
        public EPreviousDocumentStatus getNext() {
            return ERROR;
        }
    };

    private final String description;

    public abstract EPreviousDocumentStatus getNext();

    private static EPreviousDocumentStatus map(String description) {
        for (EPreviousDocumentStatus ePreviousDocumentStatus : EPreviousDocumentStatus.values()) {
            if (ePreviousDocumentStatus.getDescription().equals(description)) {
                return ePreviousDocumentStatus;
            }
        }
        throw new NotFoundException("EPreviousDocumentStatus-not-exist", String.format("El estado de documento con descripción %s no existe", description));
    }

    public static String getNext(IEditableDocumentBo previousDocument) {
        return Optional.ofNullable(EPreviousDocumentStatus.map(previousDocument.getDocumentStatusId())
                        .getNext())
                .map(EPreviousDocumentStatus::getDescription)
                .orElseThrow(() -> new DocumentException(EDocumentException.ILLEGAL_TRANSITION_STATUS, "La transición del estado actual no está permitida"));
    }

    public static boolean hasToBeValidated(IEditableDocumentBo previousDocument) {
        return getNext(previousDocument).equals(ERROR.description);
    }
}
