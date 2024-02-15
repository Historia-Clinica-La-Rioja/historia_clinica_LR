package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyWithoutOrderReportInfoDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudyWithoutOrderReportInfoMapper {
	private static final String SNOMED = "Orden Pendiente";

    @Named("parseTo")
    public StudyWithoutOrderReportInfoDto parseTo(StudyWithoutOrderReportInfoBo studyWithoutOrderReportInfoBo) {
        log.trace("input -> studyWithoutOrderReportInfoBo{}", studyWithoutOrderReportInfoBo);
        StudyWithoutOrderReportInfoDto result = new StudyWithoutOrderReportInfoDto();
        if (studyWithoutOrderReportInfoBo.getDocumentId() != null) {
            HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
            hceDocumentDataDto.setId(studyWithoutOrderReportInfoBo.getDocumentId().longValue());
            hceDocumentDataDto.setFilename(studyWithoutOrderReportInfoBo.getFileName());
            result.setHceDocumentDataDto(hceDocumentDataDto);
        }
        result.setStatus(studyWithoutOrderReportInfoBo.getStatus());
        result.setImageId(studyWithoutOrderReportInfoBo.getImageId());
        boolean existsDocument = result.getHceDocumentDataDto() != null;
        String documentStatus = studyWithoutOrderReportInfoBo.getDocumentStatus();
        result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
        result.setIsAvailableInPACS(studyWithoutOrderReportInfoBo.getIsAvailableInPACS());
        result.setSnomed(SNOMED);
        log.trace("Output: {}", result);
        return result;
    }

}
