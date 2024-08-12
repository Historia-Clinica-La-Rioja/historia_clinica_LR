package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnestheticReportStorageImpl implements AnestheticReportStorage {

    private final AnestheticReportRepository anestheticReportRepository;
    private final SharedStaffPort sharedStaffPort;
    private final DocumentService documentService;

    @Override
    public Integer save(AnestheticReportBo anestheticReport) {
        log.trace("Input parameters -> anestheticReport {}", anestheticReport);

        AnestheticReport entityToSave = this.mapToEntity(anestheticReport);

        Integer doctorId = sharedStaffPort.getProfessionalId(UserInfo.getCurrentAuditor());
        entityToSave.setDoctorId(doctorId);

        Integer result = anestheticReportRepository.save(entityToSave).getId();

        log.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Integer updateDocumentId(AnestheticReportBo anestheticReport) {
        log.trace("Input parameters -> anestheticReport {}", anestheticReport);
        anestheticReportRepository.updateDocumentId(anestheticReport.getId(), anestheticReport.getBusinessObjectId());
        var result = anestheticReport.getBusinessObjectId();
        log.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<AnestheticReportBo> get(Long documentId) {
        return documentService.findById(documentId)
                .map(this::mapToBo);
    }

    @Override
    public Long getDocumentIdFromLastAnestheticReportDraft(Integer internmentEpisodeId) {
        return anestheticReportRepository.getDocumentIdFromLastAnestheticReportDraft(internmentEpisodeId)
                .orElse(null);
    }

    @Override
    public String getAntestheticChart(Long documentId) {
        return anestheticReportRepository.findByDocumentId(documentId)
                .map(AnestheticReport::getAnestheticChart)
                .orElse(null);
    }

    private AnestheticReport mapToEntity(AnestheticReportBo anestheticReport) {
        return AnestheticReport.builder()
                .id(anestheticReport.getBusinessObjectId())
                .institutionId(anestheticReport.getInstitutionId())
                .patientId(anestheticReport.getPatientId())
                .documentId(anestheticReport.getId())
                .clinicalSpecialtyId(anestheticReport.getClinicalSpecialtyId())
                .anestheticChart(anestheticReport.getAnestheticChart())
                .billable(false)
                .patientMedicalCoverageId(anestheticReport.getPatientMedicalCoverageId())
                .build();
    }

    private AnestheticReportBo mapToBo(Document document) {
        return anestheticReportRepository.findByDocumentId(document.getId())
                .map(anestheticReport -> build(document, anestheticReport))
                .orElse(null);
    }

    private AnestheticReportBo build(Document document, AnestheticReport anestheticReport) {
        return AnestheticReportBo.builder()
                .businessObjectId(anestheticReport.getId())
                .id(document.getId())
                .encounterId(document.getSourceId())
                .institutionId(document.getInstitutionId())
                .patientId(document.getPatientId())
                .performedDate(document.getCreatedOn())
                .createdBy(document.getCreatedBy())
                .anestheticChart(anestheticReport.getAnestheticChart())
                .confirmed(document.isConfirmed())
                .initialDocumentId(document.getInitialDocumentId())
                .patientMedicalCoverageId(anestheticReport.getPatientMedicalCoverageId())
                .documentStatusId(document.getDocumentStatusId())
                .build();
    }

}
