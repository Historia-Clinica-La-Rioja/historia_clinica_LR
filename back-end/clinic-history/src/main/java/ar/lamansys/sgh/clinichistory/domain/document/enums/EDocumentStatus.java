package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum EDocumentStatus {

    DRAFT("445667001"),
    DRAFT_DISCARDED("260385009"),
    ERROR("723510000"),
    FINAL("445665009");

    private final String description;

    public static EDocumentStatus map(String description) {
        for (EDocumentStatus eDocumentStatus : EDocumentStatus.values()) {
            if (eDocumentStatus.getDescription().equals(description)) {
                return eDocumentStatus;
            }
        }
        throw new NotFoundException("EDocumentStatus-not-exist", String.format("El estado de documento con descripción %s no existe", description));
    }

    public static String getDocumentStatusId(IDocumentBo documentBo) {
        assert documentBo != null;
        EDocumentStatus statusId = documentBo.isConfirmed() ? FINAL : DRAFT;
        return statusId.getDescription();
    }

    public static boolean isDeleted(IDocumentBo documentBo) {
        return Stream.of(DocumentStatus.FINAL, DocumentStatus.DRAFT)
                .noneMatch(status -> documentBo.getDocumentStatusId().equals(status));
    }

}
