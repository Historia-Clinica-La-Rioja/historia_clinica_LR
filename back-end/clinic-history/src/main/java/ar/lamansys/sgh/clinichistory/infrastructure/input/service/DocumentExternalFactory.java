package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper.DocumentMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import org.springframework.stereotype.Service;

@Service
public class DocumentExternalFactory {

    private final DocumentFactory documentFactory;

    private final SharedPatientPort sharedPatientPort;

    private final DocumentMapper documentMapper;

    public DocumentExternalFactory(DocumentFactory documentFactory,
                                   SharedPatientPort sharedPatientPort,
                                   DocumentMapper documentMapper) {
        this.documentFactory = documentFactory;
        this.sharedPatientPort = sharedPatientPort;
        this.documentMapper = documentMapper;
    }

    public Long run(DocumentDto documentDto, boolean createFile){
        DocumentBo document = mapToDocument(documentDto);
        return documentFactory.run(document, createFile);
    }

    private DocumentBo mapToDocument(DocumentDto documentDto) {
        DocumentBo result = documentMapper.from(documentDto);
        result.setPatientInfo(mapToPatient(documentDto.getPatientId()));
        return result;
    }

    private PatientInfoBo mapToPatient(Integer patientId) {
        BasicPatientDto patientDto = sharedPatientPort.getBasicDataFromPatient(patientId);
        return new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
    }

}
