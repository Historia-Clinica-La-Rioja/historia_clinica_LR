package net.pladema.clinichistory.hospitalization.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.anestheticreport.AnestheticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnestheticStorageImpl implements AnestheticStorage {

    private final AnestheticReportRepository anestheticReportRepository;
    private final InternmentEpisodeRepository internmentEpisodeRepository;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final DocumentService documentService;

    public Integer save(AnestheticReportBo anestheticReport) {
        log.trace("Input parameters -> anestheticReport {}", anestheticReport);

        AnestheticReport entityToSave = this.mapToEntity(anestheticReport);

        internmentEpisodeRepository.getInternmentEpisodeMedicalCoverage(anestheticReport.getEncounterId())
                .ifPresent(patientMedicalCoverageVo -> entityToSave.setPatientMedicalCoverageId(patientMedicalCoverageVo.getId()));

        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        entityToSave.setDoctorId(doctorId);

        Integer result = anestheticReportRepository.save(entityToSave).getId();

        log.trace("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<AnestheticReportBo> get(Long documentId) {
        return documentService.findById(documentId)
                .map(this::mapToBo);
    }

    private AnestheticReport mapToEntity(AnestheticReportBo anestheticReport) {
        return AnestheticReport.builder()
                .institutionId(anestheticReport.getInstitutionId())
                .patientId(anestheticReport.getPatientId())
                .documentId(anestheticReport.getId())
                .clinicalSpecialtyId(anestheticReport.getClinicalSpecialtyId())
                .anestheticChart(anestheticReport.getAnestheticChart())
                .billable(false)
                .build();
    }

    private AnestheticReportBo mapToBo(Document document) {
        return AnestheticReportBo.builder()
                .id(document.getId())
                .encounterId(document.getSourceId())
                .institutionId(document.getInstitutionId())
                .patientId(document.getPatientId())
                .performedDate(document.getCreatedOn())
                .anestheticChart(this.getAntestheticChart(document))
                .build();
    }

    private String getAntestheticChart(Document document) {
        if (document.getStatusId().equals(DocumentStatus.FINAL)) {
            return anestheticReportRepository.findByDocumentId(document.getId())
                    .map(AnestheticReport::getAnestheticChart).orElse(null);
        }
        return null;

    }

}
