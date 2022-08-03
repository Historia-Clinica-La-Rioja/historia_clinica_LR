package ar.lamansys.nursing.infrastructure.output.repository.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.nursing.application.port.NursingDocumentStorage;
import ar.lamansys.nursing.domain.document.NursingDocumentBo;
import ar.lamansys.nursing.infrastructure.output.repository.document.mapper.NursingDocumentMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;

@Service
public class NursingDocumentStorageImpl implements NursingDocumentStorage {

    private static final Logger LOG = LoggerFactory.getLogger(NursingDocumentStorageImpl.class);

    private final DocumentExternalFactory documentExternalFactory;
    private final NursingDocumentMapper nursingDocumentMapper;

    public NursingDocumentStorageImpl(DocumentExternalFactory documentExternalFactory,
                                      NursingDocumentMapper nursingDocumentMapper) {
        this.documentExternalFactory = documentExternalFactory;
        this.nursingDocumentMapper = nursingDocumentMapper;
    }

    @Override
    public Long save(NursingDocumentBo nursingDocumentBo) {
        LOG.debug("Save new nursing cosultation document -> {}", nursingDocumentBo);
        DocumentDto documentDto = mapTo(nursingDocumentBo);
        return documentExternalFactory.run(documentDto, true);
    }

    private DocumentDto mapTo(NursingDocumentBo nursingDocumentBo) {
        DocumentDto result = nursingDocumentMapper.fromNursingDocumentBo(nursingDocumentBo);
        result.setDocumentType(DocumentType.NURSING);
        result.setDocumentSource(SourceType.NURSING);
        return result;
    }


}
