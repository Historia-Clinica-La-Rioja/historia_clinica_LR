package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEDocumentDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyWithoutOrderReportInfoDto;

import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StudyWithoutOrderReportInfoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(StudyWithoutOrderReportInfoMapper.class);

	private final String SNOMED = "Orden Pendiente";


	@Named("parseTo")
    public StudyWithoutOrderReportInfoDto parseTo(StudyWithoutOrderReportInfoBo studyWithoutOrderReportInfoBo){
        LOG.debug("input -> studyWithoutOrderReportInfoBo{}", studyWithoutOrderReportInfoBo);
		StudyWithoutOrderReportInfoDto result = new StudyWithoutOrderReportInfoDto();
		if (studyWithoutOrderReportInfoBo.getDocumentId() != null) {
			HCEDocumentDataDto hceDocumentDataDto = new HCEDocumentDataDto();
			hceDocumentDataDto.setId(studyWithoutOrderReportInfoBo.getDocumentId().longValue());
			hceDocumentDataDto.setFilename(studyWithoutOrderReportInfoBo.getFileName());
			result.setHceDocumentDataDto(hceDocumentDataDto);
		}
		result.setStatus(studyWithoutOrderReportInfoBo.getStatus());
		result.setImageId(studyWithoutOrderReportInfoBo.getImageId());
		Boolean existsDocument = result.getHceDocumentDataDto() == null? false : true;
		String documentStatus = studyWithoutOrderReportInfoBo.getDocumentStatus();
		result.setViewReport(existsDocument && DocumentStatus.FINAL.equals(documentStatus));
		result.setSeeStudy(result.getStatus());
		result.setSnomed(SNOMED);
        LOG.debug("Output: {}", result);
        return result;
    }



}
