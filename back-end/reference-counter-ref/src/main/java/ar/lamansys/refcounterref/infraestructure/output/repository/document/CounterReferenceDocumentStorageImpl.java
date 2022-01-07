package ar.lamansys.refcounterref.infraestructure.output.repository.document;

import ar.lamansys.refcounterref.application.port.CounterReferenceDocumentStorage;
import ar.lamansys.refcounterref.domain.document.CounterReferenceDocumentBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.document.mapper.CounterReferenceDocumentMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CounterReferenceDocumentStorageImpl implements CounterReferenceDocumentStorage {

    private final DocumentExternalFactory documentExternalFactory;
    private final CounterReferenceDocumentMapper counterReferenceDocumentMapper;


    @Override
    public void save(CounterReferenceDocumentBo counterReferenceDocumentBo) {
        log.debug("Save new counter reference document -> {}", counterReferenceDocumentBo);
        DocumentDto documentDto = mapTo(counterReferenceDocumentBo);
        documentExternalFactory.run(documentDto, true);
    }

    private DocumentDto mapTo(CounterReferenceDocumentBo counterReferenceDocumentBo) {
        DocumentDto result = counterReferenceDocumentMapper.fromCounterReferenceDocumentBo(counterReferenceDocumentBo);
        result.setDocumentType(DocumentType.COUNTER_REFERENCE);
        result.setDocumentSource(SourceType.COUNTER_REFERENCE);
        return result;
    }


}