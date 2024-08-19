package net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto.StudyTranscribedOrderReportInfoDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@Component
public class TranscribedDiagnosticReportInfoMapper {

    @Named("parseToDto")
    public StudyTranscribedOrderReportInfoDto parseToDto(StudyTranscribedOrderReportInfoBo studyTranscribedOrderReportInfoBo) {
        log.trace("Input parameters -> studyTranscribedOrderReportInfoBo {}", studyTranscribedOrderReportInfoBo);
        StudyTranscribedOrderReportInfoDto result = new StudyTranscribedOrderReportInfoDto();

        List<DiagnosticReportBo> diagnosticReports = studyTranscribedOrderReportInfoBo.getDiagnosticReports();
        DiagnosticReportBo firstDiagnosticReport = diagnosticReports.get(0);

        result.setSnomed(firstDiagnosticReport.getDiagnosticReportSnomedPt());
        result.setDiagnosticReports(diagnosticReports.stream().map(DiagnosticReportBo::getDiagnosticReportSnomedPt).collect(Collectors.toList()));

        result.setHealthCondition(firstDiagnosticReport.getHealthCondition().getSnomedPt());

        result.setCreationDate(studyTranscribedOrderReportInfoBo.getCreationDate());
        result.setStatus(studyTranscribedOrderReportInfoBo.getStatus());
        result.setImageId(studyTranscribedOrderReportInfoBo.getImageId());
        if (studyTranscribedOrderReportInfoBo.getDocumentId() != null) {
            HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
            hceDocumentDataDto.setId(studyTranscribedOrderReportInfoBo.getDocumentId().longValue());
            hceDocumentDataDto.setFilename(studyTranscribedOrderReportInfoBo.getFileName());
            result.setHceDocumentDataDto(hceDocumentDataDto);
        }
        result.setProfessionalName(studyTranscribedOrderReportInfoBo.getProfessionalName());
        boolean existsDocument = result.getHceDocumentDataDto() != null;
        String documentStatus = studyTranscribedOrderReportInfoBo.getDocumentStatus();
        result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
        result.setIsAvailableInPACS(studyTranscribedOrderReportInfoBo.getIsAvailableInPACS());

        log.trace("Output -> {}", result);
        return result;
    }

}
