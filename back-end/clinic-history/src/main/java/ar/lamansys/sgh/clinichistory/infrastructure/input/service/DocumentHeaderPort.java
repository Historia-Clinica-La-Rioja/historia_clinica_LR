package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentHeaderBo;

public interface DocumentHeaderPort {
    IDocumentHeaderBo getDocumentHeader(Long documentId);
}
