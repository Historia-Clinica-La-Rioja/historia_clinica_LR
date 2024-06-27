package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.application.document.visitors.FillOutDocumentVisitor;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentInstanceSupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchSpecificDocument {

    private final FetchDocument fetchDocument;
    private final FillOutDocumentVisitor fillOutDocumentVisitor;

    public Optional<IDocumentBo> run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);
        Optional<IDocumentBo> result = fetchDocument.run(documentId)
                .map(this::build);
        log.debug("Output -> {}", result);
        return result;
    }

    private IDocumentBo build(DocumentBo documentBo) {
        Supplier<IDocumentBo> supplier = EDocumentInstanceSupplier.map(documentBo.getDocumentType())
                .getDocumentBoSupplier();

        IDocumentBo result = supplier.get();
        result.setId(documentBo.getId());
        result.setEncounterId(documentBo.getEncounterId());
        result.setDocumentSource(documentBo.getDocumentSource());
        result.setDocumentType(documentBo.getDocumentType());
        result.setPerformedDate(documentBo.getPerformedDate());
        result.setClinicalSpecialtyId(documentBo.getClinicalSpecialtyId());
        result.setPatientId(documentBo.getPatientId());
        result.setInstitutionId(documentBo.getInstitutionId());
        result.setNotes(documentBo.getNotes());

        result.accept(fillOutDocumentVisitor);

        return result;
    }

}
