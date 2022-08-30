package ar.lamansys.odontology.infrastructure.repository.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import ar.lamansys.odontology.infrastructure.repository.document.mapper.OdontologyDocumentMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DentalActionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;

@Service
public class OdontologyDocumentStorageImpl implements OdontologyDocumentStorage {

    private final static Logger LOG = LoggerFactory.getLogger(OdontologyDocumentStorageImpl.class);

    private final DocumentExternalFactory documentExternalFactory;

    private final OdontologyDocumentMapper odontologyDocumentMapper;

    public OdontologyDocumentStorageImpl(DocumentExternalFactory documentExternalFactory,
                                         OdontologyDocumentMapper odontologyDocumentMapper) {
        this.documentExternalFactory = documentExternalFactory;
        this.odontologyDocumentMapper = odontologyDocumentMapper;
    }

    @Override
    public Long save(OdontologyDocumentBo odontologyDocumentBo) {
        LOG.debug("Save new odontology document -> {}", odontologyDocumentBo);
        DocumentDto documentDto = mapTo(odontologyDocumentBo);
        return documentExternalFactory.run(documentDto, true);
    }

    private DocumentDto mapTo(OdontologyDocumentBo odontologyDocumentBo) {
        DocumentDto result = odontologyDocumentMapper.fromOdontologyDocumentBo(odontologyDocumentBo);
        result.setDocumentType(DocumentType.ODONTOLOGY);
        result.setDocumentSource(SourceType.ODONTOLOGY);
        return result;
    }

    private DentalActionDto mapToDentalAction(OdontologySnomedBo snomed, OdontologySnomedBo tooth, OdontologySnomedBo surface, boolean isDiagnostic) {
        DentalActionDto result = new DentalActionDto();
        result.setSnomed(odontologyDocumentMapper.fromOdontologySnomedBo(snomed));
        result.setTooth(odontologyDocumentMapper.fromOdontologySnomedBo(tooth));
        result.setSurface(odontologyDocumentMapper.fromOdontologySnomedBo(surface));
        result.setDiagnostic(isDiagnostic);
        return result;
    }

}
