package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.application.document.edition.CreateDocumentWithEditionSupportTemplateMethod;
import ar.lamansys.sgh.clinichistory.application.document.edition.UpdatePreviousDocument;
import ar.lamansys.sgh.clinichistory.domain.document.IEditableDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.SetNullIdsIpsVisitor;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateAnestheticReportWithEditionSupportConcreteMethod extends CreateDocumentWithEditionSupportTemplateMethod {

    private final GetAnestheticReport getAnestheticReport;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;
    private final SharedPatientPort sharedPatientPort;
    private final AnestheticReportStorage anestheticReportStorage;

    public CreateAnestheticReportWithEditionSupportConcreteMethod(UpdatePreviousDocument updatePreviousDocument,
                                                                  SetNullIdsIpsVisitor setNullIdsIpsVisitor,
                                                                  GetAnestheticReport getAnestheticReport,
                                                                  CreateAnestheticReportDocument createAnestheticReportDocument,
                                                                  SharedPatientPort sharedPatientPort,
                                                                  AnestheticReportStorage anestheticReportStorage) {
        super(updatePreviousDocument, setNullIdsIpsVisitor);
        this.getAnestheticReport = getAnestheticReport;
        this.createAnestheticReportDocument = createAnestheticReportDocument;
        this.sharedPatientPort = sharedPatientPort;
        this.anestheticReportStorage = anestheticReportStorage;
    }

    @Override
    protected Long getPreviousDocumentId(IEditableDocumentBo newDocument) {
        if (newDocument.getPreviousDocumentId() != null) {
            return newDocument.getPreviousDocumentId();
        }
        Integer internmentEpisodeId = newDocument.getEncounterId();
        return anestheticReportStorage.getDocumentIdFromLastAnestheticReportDraft(internmentEpisodeId);
    }

    @Override
    protected IEditableDocumentBo getPreviousDocument(Long previousDocumentId) {
        return getAnestheticReport.run(previousDocumentId);
    }

    @Override
    protected void assertContextValid(IEditableDocumentBo newDocument) {
        anestheticReportStorage.validateAnestheticReport(newDocument.getPreviousDocumentId(), newDocument.getModificationReason());
    }

    @Override
    protected Integer createNewDocument(IEditableDocumentBo newDocument) {
        return createAnestheticReportDocument.run(newDocument);
    }

    @Override
    protected void setPatientInfo(IEditableDocumentBo newDocument) {
        Optional.ofNullable(sharedPatientPort.getBasicDataFromPatient(newDocument.getPatientId()))
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresent(newDocument::setPatientInfo);
    }
}
