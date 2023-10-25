package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderReportInfoDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudyOrderReportInfoMapper {

    @Named("parseToDto")
    public StudyOrderReportInfoDto parseToDto(StudyOrderReportInfoBo studyOrderReportInfoBo, ProfessionalDto professionalDto){
        log.trace("Input -> StudyOrderReportInfoBo {}", studyOrderReportInfoBo);
        StudyOrderReportInfoDto result = new StudyOrderReportInfoDto();

        result.setSnomed(studyOrderReportInfoBo.getSnomed());
        result.setCreationDate(studyOrderReportInfoBo.getCreationDate());
        result.setStatus(studyOrderReportInfoBo.getStatus());
        result.setImageId(studyOrderReportInfoBo.getImageId());
        result.setHealthCondition(studyOrderReportInfoBo.getHealthCondition());
        result.setDoctor(DoctorInfoDto.from(professionalDto));
        result.setSource(studyOrderReportInfoBo.getSource());

        if (studyOrderReportInfoBo.getDocumentId() != null) {
            HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
            hceDocumentDataDto.setId(studyOrderReportInfoBo.getDocumentId().longValue());
            hceDocumentDataDto.setFilename(studyOrderReportInfoBo.getFileName());
            result.setHceDocumentDataDto(hceDocumentDataDto);
        }

        boolean existsDocument = result.getHceDocumentDataDto() != null;
        String documentStatus = studyOrderReportInfoBo.getDocumentStatus();
        result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
        result.setSeeStudy(result.getStatus());

        log.trace("Output: {}", result);
        return result;
    }
}
