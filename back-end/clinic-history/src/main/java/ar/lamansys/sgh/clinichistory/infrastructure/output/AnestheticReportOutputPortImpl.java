package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output.AnestheticReportOutputPort;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EditableDocumentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnestheticReportOutputPortImpl implements AnestheticReportOutputPort {

    private final SharedHospitalizationPort sharedHospitalizationPort;

    @Override
    public Boolean validateAnestheticReport(IEditableDocumentBo documentBo) {
        EditableDocumentDto editableDocumentDto = new EditableDocumentDto(
                documentBo.getModificationReason(),
                documentBo.getCreatedBy(),
                documentBo.getPerformedDate(),
                documentBo.getEncounterId());
        return sharedHospitalizationPort.validateHospitalizationDocument(editableDocumentDto);
    }
}
